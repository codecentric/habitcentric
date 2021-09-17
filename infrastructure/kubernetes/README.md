# helmfile Deployment Configuration for habitcentric

This repository includes a deployment configuration for habitcentric using [helmfile](https://github.com/roboll/helmfile).  
The configuration provides two environments with different deployment configurations:

**Default environment**  
Deploys habitcentric using its own Spring API gateway and makes it accessible using a Kubernetes Ingress.

**Istio environment**  
Deploys habitcentric without its Spring API gateway and makes it accessible using the Istio service mesh.

**Linkerd environment**  
Deploys habitcentric without its Spring API gateway and makes it accessible using the Linkerd 2 service mesh.

**Kuma environment**  
Deploys habitcentric without its Spring API gateway and makes it accessible using the Kuma service mesh.

## Prerequisites

- Running Kubernetes cluster
- Properly configured Kubernetes client `kubectl` to administrate your cluster
- Locally installed [helmfile CLI](https://github.com/roboll/helmfile#installation)  (version >= 0.9.3)
- Locally installed [Helm CLI](https://helm.sh/docs/using_helm/#install-helm) (version >= 3.0.0) with [Helm Diff plugin](https://github.com/databus23/helm-diff#install) (version >= 3.0.0-rc.7)
- *Optional for Istio environment: Existing Istio installation on Kubernetes cluster*  
  *Detailed instructions on how to install Istio on your Kubernetes cluster and additional example configurations are located [here](https://gitlab.com/habitcentric/istio).*
- *Optional for Linkerd environment: Existing Linkerd installation on Kubernetes cluster*  
  *Detailed instructions on how to install Linkerd on your Kubernetes cluster and additional example configurations are located [here](https://gitlab.com/habitcentric/linkerd).*

## How To Deploy

### Default environment

```bash
helmfile apply
```

### Istio environment

```bash
helmfile --environment istio apply
```

### Linkerd environment

```bash
helmfile --environment linkerd apply
```

### Kuma environment

```bash
helmfile --environment kuma apply
```

### Traefik Mesh environment
**Traefik Mesh does not support our current show-case.
More information [here](https://trello.com/c/75fnCr0G/3-traefik-mesh-evaluation-first-look)**

```bash
helmfile --environment traefik-mesh apply
```

## How To Destroy

```bash
helmfile destroy
```

### Istio environment

```bash
helmfile --environment istio destroy
```

### Linkerd environment

```bash
helmfile --environment linkerd destroy
```

### Kuma environment

```bash
helmfile --environment kuma destroy
```

### Traefik Mesh environment

```bash
helmfile --environment traefik-mesh destroy
```
