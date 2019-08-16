# helmfile Deployment Configuration for habitcentric

This repository includes a deployment configuration for habitcentric using [helmfile](https://github.com/roboll/helmfile).  
The configuration provides two environments with different deployment configurations:

**Default environment**  
Deploys habitcentric using its own Spring API gateway and makes it accessible using a Kubernetes Ingress.

**Istio environment**  
Deploys habitcentric without its Spring API gateway and makes it accessible using the Istio service mesh.

## Prerequisites

- Running Kubernetes cluster
- Properly configured Kubernetes client `kubectl` to administrate your cluster
- Locally installed [helmfile CLI](https://github.com/roboll/helmfile#installation)
- *Optional for Istio environment: Existing Istio installation on Kubernetes cluster*  
  *Detailed instructions on how to install Istio on your Kubernetes cluster and additional example configurations are located [here](https://gitlab.com/habitcentric/istio).*

## How To Deploy

### Default environment

```bash
helmfile apply
```

### Istio environment

```bash
helmfile --environment istio apply
```

## How To Destroy

```bash
helmfile destroy
```

### Istio environment

```bash
helmfile --environment istio destroy
```
