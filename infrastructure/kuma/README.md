# Kuma Service Mesh Configuration for habitcentric

This repository provides guidance for setting up Kuma as a service mesh for
habitcentric. This guide is based on Kuma 1.0.3.

## Prerequisites

- A running Kubernetes cluster (tested with Kubernetes 1.19.4).
- `kubectl` configured to connect to this cluster.
- An nginx-ingress running in the cluster. \
  _You may use other ingress controllers, but this tutorial focuses on nginx._

## Kuma Installation

To install Kuma in your running cluster, download the release and install the
components via the CLI tool `kumactl`.

```sh
# Fetch and extract Kuma release
curl -L https://kong.bintray.com/kuma/kuma-1.0.3-debian-amd64.tar.gz | tar -xz
export PATH="$PATH:kuma-1.0.3/bin"

# Install the Kuma control plane and components required for metrics and tracing
kumactl install control-plane | kubectl apply -f -
kumactl install metrics | kubectl apply -f -
kumactl install tracing | kubectl apply -f -

# Apply the a configuration for the default mesh that enables mTLS, tracing
# and metrics
kubectl apply -f mesh.yaml

# You may access Kuma's read-only dashboard by port-forwarding
# to the control plane pod
kubectl port-forward svc/kuma-control-plane -n kuma-system 5681:5681
```

## Add the Ingress Controller to the Service Mesh

To let the service mesh manage traffic between your ingress controller and the
services, you need to add the ingress controller to the mesh. This is done by
enabling sidecar injection and choosing the specific mesh via annotations. For
ingress controllers, there is also the `kuma.io/gateway` annotation that needs
to be set to prevent the sidecar from touching incoming traffic.

If you are using the nginx ingress controller in minikube, you can patch the
deployment to add those annotations to the pod:

```sh
kubectl patch deploy -n kube-system ingress-nginx-controller --patch '
    {
        "spec": {
            "template": {
                "metadata": {
                    "annotations": {
                        "kuma.io/gateway": "enabled",
                        "kuma.io/mesh": "default",
                        "kuma.io/sidecar-injection": "enabled"
                    }
                }
            }
        }
    }
'
```

After applying the patch, the pod should be recreated with a sidecar container
from Kuma.

## habitcentric Deployment

After installing Kuma, the cluster is now ready to install our demo application:
habitcentric. To do so, follow the instructions in the [kubernetes deployment readme](../kubernetes/README.md)
and choose the `kuma` environment.

Once the deployment has finished, deploy the necessary ingress resources to
access the application by running the following command from this directory:

```bash
kubectl apply -f habitcentric/ingresses.yaml
```

If you are not using the nginx ingress controller, please modify the annotations
in the `ingresses.yaml` file before applying.

## mTLS

mTLS can be activated in the mesh configuration (`mesh.yaml`). However, it is
currently commented out since activating it will result in 502 errors for
habitcentric. I have not yet figured out why.

## Metrics

Metrics are already enabled via settings in the file `mesh.yaml` that we applied
earlier. You may port-forward to port 3000 of the Grafana pod, open
[Grafana](http://localhost:3000) in a browser and login using the credentials
`admin` / `admin`.

## Tracing

> There are still issues with tracing. Sometimes, spans from the habitcentric
> services will not arrive in Jaeger. I still don't know why.

Tracing is already configured via settings in the file `mesh.yaml`. However, we
still need to configure for which services traces should be collected. To
collect traces from all services in the mesh, apply the prepared `TrafficTrace`
custom resource:

```sh
kubectl apply -f trace.yaml
```

To view traces, port-forward to to port 16686 of the Jaeger pod and open
[Jaeger](http://localhost:16686) in a browser.

For more information on tracing, see the [official
documentation](https://kuma.io/docs/1.0.3/policies/traffic-trace/).
