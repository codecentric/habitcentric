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
managing and debugging utilities for the mesh. For detailed information
on the CLI and the installation process, visit https://linkerd.io/2/getting-started/.

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

To add services to the service mesh, either the namespace, deployment or the pods themselves
need to receive the annotation `linkerd.io/inject: enabled`. More information on this can be
found in the documentation: https://linkerd.io/2/tasks/adding-your-service/.

The deployment of our demo application will take care of this by adding the annotation to
the deployed namespaces. However, the installed ingress controller should receive this annotation
as well in order to communicate with the services via the mesh. We use the `linkerd` CLI to inject
this annotation into the the ingress controller's deployment:

```bash
# Read, modify and re-apply the YAML of the nginx controller's deployment
kubectl get deployment -n kube-system nginx-ingress-controller -o yaml \
    | linkerd inject - \
    | kubectl apply -f -

# Verify that the ingress controller's pod has restarted with 2 containers
kubectl get pod -n kube-system -w
```

If you are using another ingress controller or your nginx ingress controller is deployed in
another namespace or with another name, please adjust the commands accordingly.

If you are using minikube, the addon manager will probably rollback any changes to the
ingress controller again. In this case, please deactivate the addon manager addon.
Please keep in mind to enable it again if you want to create a new minikube cluster or
change addons in the existing one.

## habitcentric Deployment

After installing Linkerd, the cluster is now ready to install our demo application:
habitcentric. To do so, follow the instruction in the
[corresponding repository](https://gitlab.com/habitcentric-infrastructure/hc-kubernetes)
and choose the `linkerd` environment.

Once the deployment has finished, deploy the necessary ingress resources to access the application
by running the following command:

```bash
kubectl apply -f ingresses.yaml
```
If you are not using the nginx ingress controller, please modify the
`ingresses.yaml` file before applying. For detailed information about ingresses in Linkerd, visit
https://linkerd.io/2/tasks/using-ingress/.

## Accessing the Application and Visualizing Traffic

To access the application, you need to configure the following hostnames to point to your ingress
controller's IP (in case of minikube, the IP of the minikube VM):

* habitcentric.demo
* keycloak.demo

Then, access the application via the browser: http://habitcentric.demo/ui/

To get insight into metrics and traces, open the Linkerd dashboard by running
`linkerd dashboard`. Select any deployment in the "Resources" menu to start a live debugging
session. For metrics, click on the corresponding Grafana icons in the dashboard.