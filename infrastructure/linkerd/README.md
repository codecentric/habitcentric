# Linkerd 2 Service Mesh Configuration for habitcentric

## Prerequisites

* A running Kubernetes cluster.
* `kubectl` configured to connect to this cluster.
* An nginx-ingress running in the cluster. \
  _You may use other ingress controllers, but this tutorial focuses on nginx.
  Pay attention to https://linkerd.io/2/tasks/using-ingress/ if your environment
  differs._
* Tiller installed in the cluster (for Helm deployment of our demo application)

## Linkerd Installation

Linkerd 2 can be installed via the `linkerd` CLI tool, which also offers
managing and debugging utilities for the mesh. For detailed information on the
CLI and the installation process, visit https://linkerd.io/2/getting-started/.

```bash
# Install the CLI tool and add it to your PATH
curl -sL https://run.linkerd.io/install | sh
export PATH=$PATH:$HOME/.linkerd2/bin

# Perform a check whether the cluster meets Linkerd's requirements
linkerd check --pre

# If all checks pass, install Linkerd
# (you may verify the output before piping it to kubectl, if you want)
linkerd install | kubectl apply -f -

# Perform a post-installation check that will also wait for all components
# to start properly
linkerd check

# If you want, you can now open the Linkerd dashboard
linkerd dashboard
```

## Add the Ingress Controller to the Service Mesh

To add services to the service mesh, either the namespace or pod needs to
receive the annotation `linkerd.io/inject: enabled`. More information on this
can be found in the documentation:
https://linkerd.io/2/tasks/adding-your-service/.

The deployment of our demo application will take care of this by adding the
annotation to the deployed namespaces. However, the installed ingress controller
should receive this annotation as well in order to communicate with the services
via the mesh. We use the `linkerd` CLI to inject this annotation into the the
ingress controller's deployment:

```bash
# Read, modify and re-apply the YAML of the nginx controller's deployment
kubectl get deployment -n kube-system ingress-nginx-controller -o yaml \
    | linkerd inject - \
    | kubectl apply -f -

# Verify that the ingress controller's pod has restarted with 2 containers
kubectl get pod -n kube-system -w
```

If you are using another ingress controller or your nginx ingress controller is
deployed in another namespace or with another name, please adjust the commands
accordingly.

If you are using minikube, the addon management may occasionally rollback
changes to the nginx ingress controller. If that happens, re-inject the
annotation again using the command above.

## habitcentric Deployment

After installing Linkerd, the cluster is now ready to install our demo
application: habitcentric. To do so, follow the instructions in the
[corresponding
repository](https://gitlab.com/habitcentric-infrastructure/hc-kubernetes) and
choose the `linkerd` environment.

Once the deployment has finished, deploy the necessary ingress resources to
access the application by running the following command from this directory:

```bash
kubectl apply -f habitcentric/ingresses.yaml
```

If you are not using the nginx ingress controller, please modify the
`ingresses.yaml` file before applying. For detailed information about ingresses
in Linkerd, visit https://linkerd.io/2/tasks/using-ingress/.

### Service Profiles

Linkerd allows us to create `ServiceProfiles` for existing Kubernetes
`Services`. These `ServiceProfiles` describe individual HTTP routes of their
corresponding `Service` so that Linkerd is then able to report per-route metrics
in its Dashboards. Additionally, you can enable further features like automatic
retries and timeouts on a per-route basis.

To deploy `ServiceProfiles` for the habitcentric services, execute the following
command:

```
kubectl apply -f habitcentric/service-profiles.yaml
```

This enables route metrics, automatic retries and timeouts for these services.

### Fault Injection

In Linkerd, the traffic split feature can be used to inject faults into
services. This can be useful to test the resilience of your service ecosystem.

To test fault injection using the steps below, please add at least one habit in
the UI of habitcentric, so that the report service will perform upstream calls
to the track service.

You can deploy an nginx server that simulates a faulty version of habitcentric's
track service by either responding with error code 500 or by taking two seconds
to respond.

For error code 500, deploy the corresponding nginx configuration first:

```
kubectl apply -f habitcentric/fault-injection/track-fault-config-500.yaml
```

For the two second response delay, deploy the following configuration instead:

```
kubectl apply -f habitcentric/fault-injection/track-fault-config-latency.yaml
```

Afterwards, you can deploy the nginx server:

```
kubectl apply -f habitcentric/fault-injection/track-fault-service.yaml
```

Then, you can deploy the `TrafficSplit` definition that will send 50% of all
requests for the track service to the faulty service instead, resulting in a 50%
success rate:

```
kubectl apply -f habitcentric/fault-injection/track-fault-injection.yaml
```

You can view the report service deployment in Linkerd's dashboard and see that
~50% of the report service requests fail (either due to a timeout or due to the
error code), because of the injected failure in the upstream track service.
However, due to the retries configured in the `ServiceProfiles`, these errors
will not be visible for the user. You may modify the `ServiceProfiles` in the
file `habitcentric/service-profiles.yaml` to experiment with this behavor. For
example, by disabling retries and testing how habitcentric behaves.

## Accessing the Application and Visualizing Traffic

To access the application, you need to configure the following hostnames to
point to your ingress controller's IP (in case of minikube, the IP of the
minikube VM):

* habitcentric.demo

Then, access the application via the browser: http://habitcentric.demo/ui/

To get insight into metrics and traces, open the Linkerd dashboard by running
`linkerd dashboard`. Select any deployment in the "Resources" menu to start a
live debugging session. For metrics, click on the corresponding Grafana icons in
the dashboard.