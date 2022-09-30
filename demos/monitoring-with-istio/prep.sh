#!/bin/bash

. $(dirname ${BASH_SOURCE})/../util.sh

cd ../..


desc "Wir erstellen einen Minikube Cluster"
DEMO_RUN_FAST=y run "minikube start --memory=8192 --cpus=6 --cni flannel" "$SUDO_USER"

desc "Und deployen Istio"
DEMO_AUTO_RUN=y run "cd ./infrastructure/istio"
run "helmfile --environment cni apply"

desc "Minikube Load Balancer"
run "minikube tunnel > /dev/null 2>&1 &"

sleep 5

desc "Initialisieren der IP"
export HABITCENTRIC_IP=$(kubectl -n istio-ingress get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
patch_line="$HABITCENTRIC_IP habitcentric.demo kiali.demo grafana.demo prometheus.demo jaeger.demo bookinfo.demo keycloak.demo shopping.demo locust.demo"
DEMO_RUN_FAST=y run "$sed -i 's/^.* habitcentric.demo .*$/$patch_line/' /etc/hosts"

desc "Und unsere Demo-Applikation habitcentric"
DEMO_AUTO_RUN=y run "cd ../kubernetes"
run "helmfile --environment istio apply"

desc "Noch schnell Zugang zu unseren Telemetrie-Services.."
DEMO_AUTO_RUN=y run "cd ../istio"
run "kubectl apply -f config/00-telemetry-gateway.yaml && kubectl apply -f config/01-telemetry-routing-rules.yaml"
