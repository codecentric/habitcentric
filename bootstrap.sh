#!/usr/bin/env sh
set -e

infra_folder="infrastructure"
linkerd_folder="$infra_folder/linkerd"
istio_folder="$infra_folder/istio"
kubernetes_folder="$infra_folder/kubernetes"

require_commands() {
  missing_commands=""
  for command in "$@"; do
    if ! hash "$command" 2>/dev/null; then
      missing_commands="$missing_commands $command"
    fi
  done

  if [ -n "$missing_commands" ]; then
    echo "You are missing the following cli-tools to bootstrap habitcentric:"
    for command in $missing_commands; do
      echo "- $command"
    done
    exit 1
  fi
}

linkerd_install() {
  echo "Running linkderd pre-install checks.."
  linkerd check --pre

  echo "installing linkderd..."
  linkerd install | kubectl apply -f -
  linkerd viz install | kubectl apply -f -
  linkerd jaeger install | kubectl apply -f -

  echo "Installed linkerd waiting for all components to become ready"
  linkerd check # --wait 5m0s is default

  echo "Patch ingress config map to enable tracing"
  # We should do this before patching the ingress, then we don't need to restart the controller.
  kubectl get configmaps -n ingress-nginx ingress-nginx-controller -o json \
    | jq '.data."enable-opentracing" = "true" | .data."zipkin-collector-host" = "collector.linkerd-jaeger"' \
    | kubectl apply -f -

  echo "Patching minikube ingress"
  # There is an issue with the rolling update settings of the ingress controller:
  # https://github.com/kubernetes/minikube/issues/12903
  # The default strategy does not allow the old controller to be shut down before
  # the new one is up an running. The new controller cannot be started because the hostPort
  # is still used by the old controller.
  # To work around this we'll patch the deployment to use the recreate strategy.
  kubectl get deployment -n ingress-nginx ingress-nginx-controller -o json \
    | jq '.spec.strategy.rollingUpdate.maxUnavailable = 1' \
    | kubectl apply -f -
  kubectl get deployment -n ingress-nginx ingress-nginx-controller -o yaml \
    | linkerd inject - \
    | kubectl apply -f -
}

linkerd_deploy_ingress() {
  kubectl apply -f "$linkerd_folder/habitcentric/ingresses.yaml"
}

istio_install() {
  # run in a subshell so the don't have to reset the working dir
  if [ -n "$1" ]; then
    (cd "$istio_folder" && helmfile --environment "$1" apply)
  else
    (cd "$istio_folder" && helmfile apply)
  fi
}

istio_deploy() {
  kubectl apply -f "$istio_folder/config/00-telemetry-gateway.yaml" && kubectl apply -f "$istio_folder/config/01-telemetry-routing-rules.yaml"
  kubectl apply -f "$istio_folder/config/10-gateway.yaml" && kubectl apply -f "$istio_folder/config/11-routing-rules.yaml"
  kubectl apply -f "$istio_folder/config/21-oidc-authn-policies.yaml" && kubectl apply -f "$istio_folder/config/22-oidc-authz-policies.yaml"
  kubectl apply -f "$istio_folder/config/23-mtls-authz-policies.yaml"
  kubectl apply -f "$istio_folder/config/30-canary-workload-subsets.yaml" && kubectl apply -f "$istio_folder/config/31-routing-rules-canary.yaml"
  kubectl apply -f "$istio_folder/config/40-routing-rules-resilience.yaml"
}

habitcentric_deploy() (
    # run in a subshell so the don't have to reset the working dir
    if [ -n "$1" ]; then
      (cd "$kubernetes_folder" && helmfile --environment "$1" apply)
    else
      (cd "$kubernetes_folder" && helmfile apply)
    fi
)

print_hosts_patch() {
  echo "$1 habitcentric.demo kiali.demo prometheus.demo grafana.demo jaeger.demo keycloak.demo"
}

print_minikube_hosts_patch() {
  ip="$(minikube ip)"
  print_hosts_patch "$ip"
}

print_ingress_hosts_patch() {
  ip="$(kubectl get services istio-ingressgateway -n istio-system -o json | jq '.status.loadBalancer.ingress[0].ip')"
  print_hosts_patch "$ip"
}

wait_for_ready_replica() {
  kube_resource_type="$1"
  min_num_replicas="$2"
  kube_resource_name="$3"
  kube_namespace="$4"

  echo "Waiting for $kube_resource_type $kube_resource_name in namespace $kube_namespace to have at least $min_num_replicas ready replicas"
  num_replicas="0";
  while [ "$num_replicas" = "null" ] || [ -z "$num_replicas" ] || [ "$num_replicas" -lt "$min_num_replicas" ]
  do
    num_replicas="$(kubectl get "$kube_resource_type" "$kube_resource_name" -n "$kube_namespace" -o json 2> /dev/null | jq .status.readyReplicas)"
    # if the resource has not ready replicas we get "null", if there is no such resource we get ""
    [ -z "$num_replicas" ] && echo " not found"
    [ "$num_replicas" = "null" ] && echo " not ready"
    sleep 3
  done
  echo " Ready!"
}

patch_hosts() {
  patch_line="$1"
  if grep "habitcentric.demo" /etc/hosts; then
    sudo sed -i "s/^.* habitcentric.demo .*$/$patch_line/" /etc/hosts
  else
    sudo sh -c "echo '\n#habitcentric minikube cluster\n$patch_line' >> /etc/hosts"
  fi
}

bootstrap_k8s() {
  if [ "$2" = "enable-cni" ]; then
    echo "Bootstrapping habitcentric with environment '$1' and k8s cni flannel..."
    minikube start --cni flannel
  else
    echo "Bootstrapping habitcentric with environment '$1'..."
    minikube start
  fi
}

habitcentric_istio_deploy() {
  if [ "$1" = "cni" ]; then
    bootstrap_k8s "istio" "enable-cni"
  else
    bootstrap_k8s "istio"
  fi
  istio_install "$1"
  habitcentric_deploy "istio"
  # wait for keycloak before progressing, istiod needs to be able to pull the key set from keycloak
  # https://github.com/istio/istio/issues/29436
  wait_for_ready_replica "statefulset" 1 "keycloak" "hc-keycloak"
  istio_deploy

  echo
  echo "Run:"
  echo " minikube tunnel > /dev/null"
  echo "To get the IP:"
  echo " kubectl get services istio-ingressgateway -n istio-ingress -o json | jq -r '.status.loadBalancer.ingress[0].ip'"
}

habitcentric_linkerd_deploy() {
  bootstrap_k8s "linkerd"
  echo "Enabling ingress Addon"
  minikube addons enable ingress
  linkerd_install
  habitcentric_deploy "linkerd"
  # wait for the ingress-controller to become available
  # otherwise the deployment of the linkerd ingresses will fail
  wait_for_ready_replica "deployment" 1 "ingress-nginx-controller" "ingress-nginx"
  linkerd_deploy_ingress

  echo
  echo "Required Hosts Entry:"
  print_minikube_hosts_patch
  #patch_hosts "$(print_minikube_hosts_patch)"
  echo
  echo
  echo "Linkerd dashboard:"
  echo " linkerd viz dashboard"
  echo "Jaeger UI:"
  echo " linkerd jaeger dashboard"
}

required_commands="kubectl helmfile minikube"
case "$1" in
  "linkerd")
    required_commands="$required_commands linkerd"
    env="linkerd"
    ;;
  "istio-cni")
    env="istio-cni"
    ;;
  "istio")
    env="istio"
    ;;
  *)
    env=""
    ;;
esac

# we want parameter expansion here
# shellcheck disable=SC2086
require_commands $required_commands
unset required_commands

if [ "$env" = "linkerd" ]; then
  habitcentric_linkerd_deploy
fi

if [ "$env" = "istio-cni" ]; then
  habitcentric_istio_deploy "cni"
fi

if [ "$env" = "istio" ]; then
  habitcentric_istio_deploy
fi

if [ -z "$env" ]; then
  habitcentric_deploy
fi
