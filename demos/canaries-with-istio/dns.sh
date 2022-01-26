#!/bin/bash
set -eo pipefail

if [[ "$OSTYPE" == "darwin"* ]]; then
  sed="gsed"
else
  sed="sed"
fi

print_hosts_patch() {
  echo "$1 habitcentric.demo kiali.demo prometheus.demo grafana.demo jaeger.demo keycloak.demo"
}

print_ingress_hosts_patch() {
  ip="$(kubectl get services istio-ingressgateway -n istio-ingress -o json | jq -r '.status.loadBalancer.ingress[0].ip')"
  print_hosts_patch "$ip"
}

patch_hosts() {
  patch_line="$1"
  if grep "habitcentric.demo" /etc/hosts; then
    $sed -i "s/^.* habitcentric.demo .*$/$patch_line/" /etc/hosts
  else
    sh -c "echo '\n# habitcentric minikube cluster\n$patch_line' >> /etc/hosts"
  fi
}

patch_hosts "$(print_ingress_hosts_patch)"
