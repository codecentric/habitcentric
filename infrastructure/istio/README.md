# Istio Service Mesh Configuration for habitcentric

## Istio Installation

To install Istio on your Kubernetes cluster you can choose between two installation methods:

- automatic install using provided shell scripts *(convenient, recommended for beginners)*
- manual install using Kubernetes Helm package manager *(configurable, recommended for advanced users)*

### Prerequisites

- Running Kubernetes cluster
- Properly configured Kubernetes client `kubectl` to administrate your cluster

#### GKE specific

If you are using Google Kubernetes Engine (GKE), you have to grant cluster administrator permissions to the current user. To grant cluster admin permissions, you must assign the role `Kubernetes Engine Admin` to your user in the [Google Cloud Console](https://console.cloud.google.com/iam-admin/iam).

```bash
kubectl create clusterrolebinding cluster-admin-binding \
    --clusterrole=cluster-admin \
    --user=$(gcloud config get-value core/account)
```

### Automatic Installation using shell scripts

Windows:

```powershell
powershell -executionpolicy bypass -File install/install-istio.ps1
```

Linux:

```bash
./install/install-istio.sh
```

### Manual Installation using Helm

Please install the Helm CLI before continuing. Look up installation steps in the [Helm documenation](https://helm.sh/docs/using_helm/#installing-helm)

Download Istio and change into download directory

```bash
curl -L https://git.io/getLatestIstio | ISTIO_VERSION=1.3.0 sh - && cd istio-1.3.0
```

Create a namespace for the Istio control plane:

```bash
kubectl create namespace istio-system
```

Install Istio's Custom Resource Definitions (CRDs)

```bash
helm template install/kubernetes/helm/istio-init --name istio-init --namespace istio-system | kubectl apply -f -
```

Verify the installation of Istio's CRDs:

```bash
kubectl get crds | grep -c 'istio.io\|certmanager.k8s.io'
```
The result should be `23`. If not, wait a few seconds and try again.

Install Istio:

```bash
helm template install/kubernetes/helm/istio \
  --name istio \
  --namespace istio-system \
  --values install/kubernetes/helm/istio/values-istio-demo.yaml \
  --set kiali.dashboard.grafanaURL="http://grafana.demo" \
  --set kiali.dashboard.jaegerURL="http://jaeger.demo" \
  --set sidecarInjectorWebhook.rewriteAppHTTPProbe=true \
  | kubectl apply -f -
```

> If you want to customize your Istio installation, you can find detailed installation options [here](https://istio.io/docs/reference/config/installation-options/).

## habitcentric Deployment

TODO

## Routing Configuration

Istio secures traffic entering the service mesh using load balanced ingress gateways. To access Istio enabled services inside the mesh you must configure ingress route rules using Istio's `Gateway` and `VirtualService` resource.

### Telemetry Services

The provided Istio installation comes with several telemetry services to observe the configuration and behavior of the service mesh.
You can apply `telemetry-gateway.yaml` and `telemetry-routes.yaml` to enable routing to these services.

```bash
kubectl apply -f telemetry-gateway.yaml && kubectl apply -f telemetry-routes.yaml
```

### habitcentric Services

You can apply `habitcentric/habitcentric-gateway.yaml` and `habitcentric/habitcentric-routes.yaml` to enable routing to habitcentric's services.

```bash
kubectl apply -f habitcentric/habitcentric-gateway.yaml && kubectl apply -f habitcentric/habitcentric-routes.yaml
```

## Authentication & Authorization Configuration

Istio provides several security features like strong identities, transparent TLS encryption, and authentication, authorization and audit (AAA) tools to protect services and data.

### Authentication of services via mTLS

To activate Istio's mesh-wide TLS encryption, apply `habitcentric/habitcentric-authn.yaml` to your cluster. This provides every service with a strong identity based on the service accounts of the habitcentric services and enables automatic network traffic encryption between sidecars.

### HTTPS endpoint for ingress gateway

To activate the HTTPS endpoint for the ingress gateway, apply `habitcentric/habitcentric-gateway-secure.yaml` to your cluster. This replaces the existing HTTP gateway with an HTTPS gateway.

## Service Access

The Istio ingress gateway listens on several hostnames and routes your requests accordingly. To retrieve the external IP of your Istio ingress gateway load balancer run:

```bash
kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}'
```

> Note: Minikube does not automatically assign external IPs to load balanced services. To enable this functionality, run `minikube tunnel` on the command line beforehand.

To access the services deployed in the mesh, add the service hostnames (see below) to your machine's host file using the IP of your ingress gateway load balancer.

### Service Hostnames

#### Telemetry

| Service    | Hostname               |
| ---------- | ---------------------- |
| Kiali      | http://kiali.demo      |
| Grafana    | http://grafana.demo    |
| Prometheus | http://prometheus.demo |
| Jaeger     | http://jaeger.demo     |

#### habitcentric
| Service  | Hostname                      |
| -------- | ----------------------------- |
| UI       | http://habitcentric.demo      |
| Keycloak | http://habitcentric.demo/auth |
