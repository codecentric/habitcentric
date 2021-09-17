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

  echo "Patching minikube ingress"
  kubectl get deployment -n kube-system ingress-nginx-controller -o yaml \
    | linkerd inject - \
    | kubectl apply -f -
}

linkerd_deploy_ingress() {
  kubectl apply -f "$linkerd_folder/habitcentric/ingresses.yaml"
}

istio_install() {
  #istioctl operator init
  # run in a subshell so the don't have to reset the working dir
  (cd "$istio_folder" && helmfile apply)
}

istio_deploy() {
  kubectl apply -f "$istio_folder/telemetry-gateway.yaml" && kubectl apply -f "$istio_folder/telemetry-routes.yaml"
  kubectl apply -f "$istio_folder/habitcentric/habitcentric-gateway.yaml" && kubectl apply -f "$istio_folder/habitcentric/habitcentric-routes.yaml"
  kubectl apply -f "$istio_folder/habitcentric/habitcentric-authz.yaml"
  kubectl apply -f "$istio_folder/habitcentric/habitcentric-oidc.yaml"
}

habitcentric_deploy() (
    cd "$kubernetes_folder"
    if [ -n "$1" ]; then
      helmfile --environment "$1" apply
    else
      helmfile apply
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


required_commands="kubectl helmfile minikube"
case "$1" in
  "linkerd")
    required_commands="$required_commands linkerd"
    env="linkerd"
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

echo "Bootstrapping habitcentric with environment '$env'..."
minikube start

if [ "$env" = "linkerd" ]; then
  echo "Enabling ingress Addon"
  minikube addons enable ingress
  linkerd_install
  habitcentric_deploy "$env"
  # wait for the ingress-controller to become available
  # otherwise the deployment of the linkerd ingresses will fail
  wait_for_ready_replica "deployment" 1 "ingress-nginx-controller" "kube-system"
  linkerd_deploy_ingress

  echo
  echo "Required Hosts Entry:"
  print_minikube_hosts_patch
  #patch_hosts "$(print_minikube_hosts_patch)"
fi

if [ "$env" = "istio" ]; then
  istio_install
  # wait for a istiod to be ready
  wait_for_ready_replica "deployment" 1 "istiod" "istio-system"
  habitcentric_deploy "$env"
  istio_deploy

  echo
  echo "Run:"
  echo " minikube tunnel > /dev/null"
  echo "To get the IP:"
  echo " kubectl get services istio-ingressgateway -n istio-system -o json | jq -r '.status.loadBalancer.ingress[0].ip'"
fi

if [ -z "$env" ]; then
  habitcentric_deploy
fi
