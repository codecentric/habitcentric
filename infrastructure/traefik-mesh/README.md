# <img src="../../docs/images/traefik-mesh-black.svg" width="60"> Traefik Mesh Deployment for habitcentric

This configuration showcases Traefik Mesh's features based on a habitcentric Kubernetes deployment.

> ⚠️ This showcase is not entirely finished yet.

## Prerequisites

* A running Kubernetes cluster.
    * With active ingress (when using minikube `minikube addons enable ingress`)
* `kubectl` configured to connect to this cluster.
* `helm` command
* For properly working tracing: the services need to propagate tracing headers and send tracing data
  to the jaeger deployment of Traefik Mesh.

## Traefik Mesh installation

Install traefik mesh into the namespace `traefik-mesh`

```
helm repo add traefik-mesh https://helm.traefik.io/mesh
helm repo update
kubectl create namespace traefik-mesh
helm install -n traefik-mesh -f traefik-habitcentric-values.yaml \
 traefik-mesh traefik-mesh/traefik-mesh
```

## habitcentric Deployment

After installing Traefik Mesh, the cluster is now ready to install our demo application:
habitcentric. To do so, follow the instructions in the
[kubernetes deployment readme](../kubernetes/README.md) and choose the `traefik-mesh` environment.

Once the deployment has finished, deploy the necessary ingress resources to access the application
by running the following command from this directory:

```bash
kubectl apply -f habitcentric/ingresses.yaml
```

## Telemetry services

The provided traefik-mesh installation comes with two telemetry
services [Grafana](https://grafana.com/) and [Jaeger](https://www.jaegertracing.io/)) to observe the
configuration and behavior of the service mesh. You can apply `telemetry-gateway.yaml` to enable
routing to these services.

```bash
kubectl apply -f telemetry-ingress.yaml
```
