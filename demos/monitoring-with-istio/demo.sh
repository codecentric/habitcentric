#!/bin/bash

. $(dirname ${BASH_SOURCE})/../util.sh

SOURCE_DIR=$PWD

clear

cd ../..
export HABITCENTRIC_IP=$(kubectl -n istio-ingress get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')


echo "Using habitcentric IP $HABITCENTRIC_IP"

maybe_first_prompt
read -s

desc "Wir starten mit der Routing-Konfiguration für habitcentric"
DEMO_AUTO_RUN=y run "cd ./infrastructure/istio"
run "kubectl apply -f config/20-gateway-with-tls.yaml && kubectl apply -f config/11-routing-rules.yaml && kubectl apply -f config/30-canary-workload-subsets.yaml && kubectl apply -f config/31-routing-rules-canary.yaml"

desc "Schauen wir uns die mal im Detail an.."
run "bat config/11-routing-rules.yaml"

desc "Okay, werfen wir einen Blick auf die UI von habitcentric"
run "open https://habitcentric.demo"

desc "Jetzt generieren wir Traffic, damit wir unseren Steady-State beobachten können"
DEMO_AUTO_RUN=y run "cd ../../test/lpt-locust"
run "docker-compose up -d --scale worker=3 --build"
run "open http://localhost:8089"

desc "Werfen wir einen Blick auf unsere Metriken!"
run "open http://jaeger.demo && open http://grafana.demo && open http://kiali.demo"

desc "Was wären Metriken ohne ein bisschen Chaos.."
DEMO_AUTO_RUN=y DEMO_RUN_FAST=y run "curl -k --request POST --url https://habitcentric.demo/report/actuator/chaosmonkey/enable"
DEMO_RUN_FAST=y run "curl -k --request POST --url https://habitcentric.demo/report/actuator/chaosmonkey/assaults --header 'Content-Type: application/json' --data '{ \"level\": 3, \"exceptionsActive\": \"true\" }'"

desc "Schauen wir mal, was sich durch ein paar Retries verbessern lässt"
DEMO_AUTO_RUN=y run "cd ../../infrastructure/istio"
run "kubectl apply -f config/40-routing-rules-resilience.yaml"

desc "Und zu guter letzt werfen wir noch einmal einen Blick in unsere Retry-Konfiguration"
run "bat config/40-routing-rules-resilience.yaml"
