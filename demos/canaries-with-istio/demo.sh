#!/bin/bash

# we want the first element
# shellcheck disable=SC2128
. "$(dirname "${BASH_SOURCE}")"/../demo-util.sh

clear

HABITCENTRIC_IP=$(kubectl -n istio-ingress get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
export HABITCENTRIC_IP
if [ -z "$HABITCENTRIC_IP" ]; then
  echo "Could not resolve habitcentric IP. Is minikube tunnel running?"
  exit 0
else
  echo "Using habitcentric IP $HABITCENTRIC_IP"
fi

habitcentric_root_dir="../.."
infra_dir="$habitcentric_root_dir/infrastructure"
istio_dir="$infra_dir/istio"
test_dir="$habitcentric_root_dir/test"
lpt_locust_dir="$test_dir/lpt-locust"

(cd $istio_dir && kubectl apply -f config/00-telemetry-gateway.yaml > /dev/null && kubectl apply -f config/01-telemetry-routing-rules.yaml > /dev/null)

maybe_first_prompt
read -rs

desc "Zuerst müssen wir Routen für habitcentric konfigurieren"
DEMO_AUTO_RUN=y run "cd $istio_dir"
run "kubectl apply -f config/20-gateway-with-tls.yaml && kubectl apply -f config/11-routing-rules.yaml"

desc "Schauen wir uns die mal im Detail an 🙂"
run "cat config/20-gateway-with-tls.yaml"
run "cat config/11-routing-rules.yaml"

desc "Jetzt sind wir soweit, dass wir einen Blick auf habitcentric werfen können! 🎉"
run "open https://habitcentric.demo"

desc "Generieren wir mal etwas Traffic, um einen Steady-State zu haben 🚚"
DEMO_AUTO_RUN=y run "cd $lpt_locust_dir"
run "docker-compose up -d --scale worker=3 --build"
run "open http://localhost:8089"

desc "Mal schauen wie unsere Metriken jetzt aussehen 🔎"
run "open http://jaeger.demo && open http://grafana.demo && open http://kiali.demo"

desc "Ohje.. unsere neue report Version bekommt viel zu viel Traffic!"
desc "Also eben die User-Authentifizierung einschalten.. 🙍"
DEMO_AUTO_RUN=y run "cd $istio_dir"
run "kubectl apply -f config/21-oidc-authn-policies.yaml"

desc "Moment, wie ging das noch gleich?"
run "cat config/21-oidc-authn-policies.yaml"

desc "Okay, jetzt soll eigentlich hauptsächlich unser testing User draufkommen!"
run "kubectl apply -f config/30-canary-workload-subsets.yaml && kubectl apply -f config/31-routing-rules-canary.yaml"

desc "Auch diese Routing-Konfiguration schauen wir uns nochmal im Detail an"
run "cat config/30-canary-workload-subsets.yaml"
run "cat config/31-routing-rules-canary.yaml"

desc "Das müssten wir doch jetzt auch im Browser sehen, oder?"
