# Istio Service Mesh Configuration for habitcentric

## Prerequisites

- Running Kubernetes cluster
- Properly configured Kubernetes client `kubectl` to administrate your cluster
- Locally installed [helmfile CLI](https://github.com/roboll/helmfile#installation)  (version >= 0.9.3)
- Locally installed [Helm CLI](https://helm.sh/docs/using_helm/#install-helm) (version >= 3.4.0) with [Helm Diff plugin](https://github.com/databus23/helm-diff#install) (version >= 3.1.0)

> This guide is based on Istio 1.11.2

## Install Istio

We provide a helmfile configuration to set up a pre-configured Istio control plane.
It deploys the following components:

1. Istio operator
2. Istio control plane
3. Addon components (e.g. Prometheus, Kiali, Jaeger & Grafana)

```bash
helmfile apply
```

### Google Kubernetes Engine (GKE)

If you are using GKE, you have to grant cluster administrator permissions to the current user. To grant cluster admin permissions, you must assign the role `Kubernetes Engine Admin` to your user in the [Google Cloud Console](https://console.cloud.google.com/iam-admin/iam).

```bash
kubectl create clusterrolebinding cluster-admin-binding \
    --clusterrole=cluster-admin \
    --user=$(gcloud config get-value core/account)
```

## Deploy habitcentric

Please refer to the [helmfile deployment configuration](https://gitlab.com/habitcentric-infrastructure/hc-kubernetes). To deploy habitcentric for Istio, please use the provided helmfile Istio environment.

## Route to telemetry and habitcentric services

Istio secures traffic entering the service mesh using load balanced ingress gateways. To access Istio enabled services inside the mesh you must configure ingress route rules using Istio's `Gateway` and `VirtualService` resource.

### Telemetry services

The provided Istio installation comes with multiple telemetry services ([Prometheus](https://prometheus.io/), [Kiali](https://kiali.io/), [Grafana](https://grafana.com/) and [Jaeger](https://www.jaegertracing.io/)) to observe the configuration and behavior of the service mesh.
You can apply `telemetry-gateway.yaml` and `telemetry-routes.yaml` to enable routing to these services.

```bash
kubectl apply -f telemetry-gateway.yaml && kubectl apply -f telemetry-routes.yaml
```

### habitcentric services

You can apply `habitcentric/habitcentric-gateway.yaml` and `habitcentric/habitcentric-routes.yaml` to enable routing to habitcentric's services.

```bash
kubectl apply -f habitcentric/habitcentric-gateway.yaml && kubectl apply -f habitcentric/habitcentric-routes.yaml
```

### Routing and Resilience Settings

To enable automatic retries for habitcentric services apply `habitcentric/habitcentric-routes-resilience.yaml`

```bash
kubectl apply -f habitcentric/habitcentric-routes-resilience.yaml
```

## Apply authentication & authorization

Istio provides several security features like strong identities, transparent TLS encryption, and authentication, authorization and audit (AAA) tools to protect services and data.

### Enable HTTPS endpoint for ingress gateway

To activate the HTTPS endpoint for the ingress gateway, apply `habitcentric/habitcentric-gateway-secure.yaml` to your cluster.
This replaces the existing HTTP gateway with an HTTPS gateway.

```bash
kubectl apply -f habitcentric/habitcentric-gateway-secure.yaml
```

### Authenticate end users

To activate the authentication of end users using the JWT of Keycloak, apply `habitcentric/habitcentric-oidc.yaml` to your cluser.
This activates JWT verification policies for the services `habit` and `track`.
Requests without valid JWT in the HTTP `Authorization` header will fail with HTTP code `401` and message `Origin authentication failed`.

```bash
kubectl apply -f habitcentric/habitcentric-oidc.yaml
```

### Restrict access to services

To restrict access between services, apply `habitcentric/habitcentric-authz.yaml` to your cluster. This sets up AuthorizationPolicies that define access rules for habitcentric's pods based on service accounts. Unauthorized requests will fail with HTTP code `403` and a message `RBAC: access denied`.

```bash
kubectl apply -f habitcentric/habitcentric-authz.yaml
```

## Access running services

The Istio ingress gateway listens on several hostnames and routes your requests accordingly.
To retrieve the external IP of your Istio ingress gateway load balancer run:

```bash
kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}'
```

> Note: Minikube does not automatically assign external IPs to load balanced services. To enable this functionality, run `minikube tunnel` on the command line beforehand.

To access the services deployed in the mesh, add the service hostnames (see below) to your machine's host file using the IP of your ingress gateway load balancer.

### Service hostnames

#### Istio telemetry

| Service    | URL                    | Credentials   |
| ---------- | ---------------------- | ------------- |
| Kiali      | http://kiali.demo      | admin / admin |
| Grafana    | http://grafana.demo    |               |
| Prometheus | http://prometheus.demo |               |
| Jaeger     | http://jaeger.demo     |               |

#### habitcentric

| Service  | URL                           | Credentials                     |
| -------- | ----------------------------- | ------------------------------- |
| UI       | http://habitcentric.demo      | default / default (or register) |
| Keycloak | http://habitcentric.demo/auth | keycloak / keycloak             |

## Uninstall Istio

```bash
istioctl manifest generate -f install/istio-config.yaml | kubectl delete -f -
```

The control plane namespace (e.g. `istio-system`) is not removed by default. If no longer needed, use the following command to remove it:

```bash
kubectl delete namespace istio-system
```
