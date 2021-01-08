# Traefik Mesh Service Mesh Configuration for habitcentric

Used during the initial evaluation of Traefik Mesh.

**Traefik Mesh does not support our current show-case.
More information [here](https://trello.com/c/75fnCr0G/3-traefik-mesh-evaluation-first-look)**

## Prerequisites

* A running Kubernetes cluster.
  * With active ingress (when using minikube `minikube addons enable ingress`)
* `kubectl` configured to connect to this cluster.
* `helm` command
* For properly working tracing: the services need to propagate tracing headers and send
  tracing data to the jaeger deployment of Traefik Mesh.

## Traefik Mesh installation

Install traefik mesh into the namespace `traefik-mesh`
```
helm repo add traefik-mesh https://helm.traefik.io/mesh
helm repo update
kubectl create namespace traefik-mesh
helm upgrade -n traefik-mesh -f traefik-habitcentric-values.yaml \
 traefik-mesh traefik-mesh/traefik-mesh
```
