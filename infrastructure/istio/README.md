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

You can access the services on the URIs shown below.

| Service    | URI                    |
|------------|------------------------|
| Kiali      | http://kiali.demo      |
| Grafana    | http://grafana.demo    |
| Prometheus | http://prometheus.demo |
| Jaeger     | http://jaeger.demo     |
