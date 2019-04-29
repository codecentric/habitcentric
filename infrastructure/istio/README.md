# Istio

## Installation

To install Istio on your Kubernetes cluster you can choose between two installation methods:

- automatic install using provided shell scripts *(convenient, recommended for beginners)*
- manual install using Kubernetes Helm package manager *(configurable, recommended for advanced users)*

Prerequisites:

- Running Kubernetes cluster
- Properly configured Kubernetes client `kubectl` to administrate your cluster

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
curl -L https://git.io/getLatestIstio | ISTIO_VERSION=1.1.4 sh - && cd istio-1.1.4
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
The result should be `53`. If not, wait a few seconds and try again.

Install Istio:

```bash
helm template install/kubernetes/helm/istio --name istio --namespace istio-system --values install/kubernetes/helm/istio/values-istio-demo.yaml --set kiali.dashboard.grafanaURL="http://grafana.demo" --set kiali.dashboard.jaegerURL="http://jaeger.demo" | kubectl apply -f -
```

> If you want to customize your Istio installation, you can find detailed installation options [here](https://istio.io/docs/reference/config/installation-options/).

## Optional telemetry gateway configuration

You can apply `telemetry-gateway.yaml` and `telemetry-routes.yaml` to enable routing to Istio's telemetry services.

`kubectl apply -f telemetry-gateway.yaml && kubectl apply -f telemetry-routes.yaml`

## Accessing services

Istio secures traffic entering the service mesh using load balanced ingress gateways. To access Istio enabled services inside the mesh you must configure ingress route rules using Istio's `Gateway` and `VirtualService` resource.

To retrieve the external IP of your Istio ingress gateway load balancer run:

```bash
kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}'
```

> Note: Minikube does not automatically assign external IPs to load balanced services. To enable this functionality, run `minikube tunnel` on the command line beforehand.

### Service URIs

| Service    | URI                    |
|------------|------------------------|
| Kiali      | http://kiali.demo      |
| Grafana    | http://grafana.demo    |
| Prometheus | http://prometheus.demo |
| Jaeger     | http://jaeger.demo     |

We recommend adding these URIs to your machine's host file using the IP of your ingress gateway load balancer.
