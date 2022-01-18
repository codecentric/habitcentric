# Istio Service Mesh Configuration for habitcentric

## Prerequisites

- Running Kubernetes cluster
- Properly configured Kubernetes client `kubectl` to administrate your cluster
- Locally installed [helmfile CLI](https://github.com/roboll/helmfile#installation)  (version >= 0.9.3)
- Locally installed [Helm CLI](https://helm.sh/docs/using_helm/#install-helm) (version >= 3.4.0) with [Helm Diff plugin](https://github.com/databus23/helm-diff#install) (version >= 3.1.0)

> This guide is based on Istio 1.12.1

## Install Istio

We provide a helmfile configuration to set up a pre-configured Istio control plane.
It deploys the following components:

1. Istio control plane
2. Istio ingress gateway
3. Addon components (e.g. Prometheus, Kiali, Jaeger & Grafana)

```bash
helmfile apply
```

We also provide an optional CNI environment that installs Istio with the Istio CNI plugin enabled.
Make sure that your Kubernetes Cluster runs a CNI first!

```bash
helmfile --environment cni apply
```

### Google Kubernetes Engine (GKE)

If you are using GKE, you have to grant cluster administrator permissions to the current user. To grant cluster admin permissions, you must assign the role `Kubernetes Engine Admin` to your user in the [Google Cloud Console](https://console.cloud.google.com/iam-admin/iam).

```bash
kubectl create clusterrolebinding cluster-admin-binding \
    --clusterrole=cluster-admin \
    --user=$(gcloud config get-value core/account)
```

## Deploy habitcentric

Please refer to the [kubernetes deployment readme](../kubernetes/README.md). To deploy habitcentric for Istio, please use the provided helmfile Istio environment.

## Configure Service Mesh for habitcentric

### Basic routing setup for ingress traffic

Istio supports three different ways to control ingress traffic: 

- Istio ingress gateway (Envoy proxy configured by Istio control plane)
- Kubernetes Ingress API using Istio Ingress class
- Kubernetes Gateway API

Only the Istio ingress gateway supports the full Istio feature set, which is why this demo showcase
focuses on the Istio ingress gateway configuration. To configure the Istio ingress gateway
deployment, we apply `Gateway` and `VirtualService` resources as described in the following
sections.

#### Step 1: Telemetry services

The provided Istio installation comes with multiple telemetry services
([Prometheus](https://prometheus.io/), [Kiali](https://kiali.io/), [Grafana](https://grafana.com/)
and [Jaeger](https://www.jaegertracing.io/)) to observe the configuration and behavior of the
service mesh. To expose these services, we apply a `Gateway` that listens on specific hostnames for
our telemetry services and apply a `VirtualService` on top of that to route requests matching these
hostnames to our k8s workloads.

```bash
kubectl apply -f config/00-telemetry-gateway.yaml && kubectl apply -f config/01-telemetry-routing-rules.yaml
```

#### Step 2: habitcentric services

After exposing our telemetry services, we want to enable ingress to our habitcentric application as
well: 

```bash
kubectl apply -f config/10-gateway.yaml && kubectl apply -f config/11-routing-rules.yaml
```

This time however, we have to partition a habitcentric k8s workload into multiple subsets
first because the habitcentric report service is actually backed by two different workloads: report
v1 and report v2. To make Istio aware of these differences, we can apply a `DestinationRule` that
defines multiple subsets based on label selectors:

```bash
kubectl apply -f config/30-canary-workload-subsets.yaml
```

> **Congrats!** You can now open up `habitcentric.demo` in your browser and explore the application!  
> For instructions on how to access the Istio ingress controller from your machine, see 
> [Access running services](#access-running-services)

#### Authentication & Authorization of end users and services

After establishing basic routing rules we can enable Istio's security features like strong
identities, transparent TLS encryption and authentication, authorization and audit (AAA) tools to
protect services and data.

#### Enable HTTPS endpoint for ingress gateway

First, we re-configure our habitcentric `Gateway` so that it uses HTTPS based on a self-signed
certificate:

```bash
kubectl apply -f config/20-gateway-with-tls.yaml
```

#### Authenticate end users

Next, we enable end-user authorization based on JWT issued by Keycloak.
Before we can configure the necessary authorization rules, we have to authenticate our
users first by adding a request principal to every request if a JWT is present in the
`Authorization` header.
To do this, we apply a `RequestAuthentication` resource which defines the allowed issuers and the
JWKS URI to verify the JWT signatures:

```bash
kubectl apply -f config/21-oidc-authn-policies.yaml
```

After applying this, Istio rejects requests with invalid JWTs.
However, requests **without** token are still accepted!
Now we want to require a valid JWT for requests to our `habit`, `track` and `report` service.
We can enable this by defining `AuthorizationPolicy` resources, that denies all requests without a
request principal:

```bash
kubectl apply -f config/22-oidc-authz-policies.yaml
```

#### Restrict access to services

As a last precaution we now want to restrict network access between our applications as well.
Since Istio provides strong identities to our services by activating mutual TLS by default, we can
simply define `AuthorizationPolicy` resources to explicitly whitelist connections that we want to
allow: 

```bash
kubectl apply -f config/23-mtls-authz-policies.yaml
```

### Advanced routing rules for a canary release strategy

You may have noticed that every third request to habitcentric shows a more detailed report by also
calculating the monthly achievement rate of our habit schedule and tracked entries.
This happens because habitcentric deploys two different workloads of the same service: two instances
of report v1 and one instance of report v2.
However, both workloads are referred to by the same service and Istio applies a round-robin load
balancing strategy by default.

We now want to utilize Istio's advanced routing features to limit the access to the brand-new report
v2 workload because we want to make sure that everything works smoothly.
First, Istio must be aware of the two different workloads.
We apply `DestinationRule` resources that introduces two so-called "subsets".
Each workload is assigned to one subset using label selectors:

```bash
kubectl apply -f config/30-canary-workload-subsets.yaml
```

Now we want to achieve two things:

- by default, 10% of requests should go to report v2, 90% of requests to report v1
- the user with the username `testing` should always be routed to report v2

We change the `VirtualService` resource we created earlier that is responsible for routing requests
to our report service.
We introduce routing weights and utilize the request principal provided by our JWTs:

```bash
kubectl apply -f config/31-routing-rules-canary.yaml
```

### Routing and Resilience Settings

Last but not least we want to guarantee that our service mesh operates smoothly even when one of our
services occasionally fails.
We know that our network is unreliable and that timeouts or entire network partitions might happen
all the time, so we define automatic retries for our services as well by extending our
`VirtualService` even more:

```bash
kubectl apply -f config/40-routing-rules-resilience.yaml
```

## Access running services

The Istio ingress gateway listens on several hostnames and routes your requests accordingly.
To retrieve the external IP of your Istio ingress gateway load balancer run:

```bash
kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}'
```

> Note: Minikube does not automatically assign external IPs to load balanced services. To enable
> this functionality, run `minikube tunnel` on the command line beforehand.

To access the services deployed in the mesh, add the service hostnames (see below) to your machine's
host file using the IP of your ingress gateway load balancer.

### Service hostnames

#### Istio telemetry

| Service    | URL                    | Credentials   |
| ---------- | ---------------------- | ------------- |
| Kiali      | http://kiali.demo      | admin / admin |
| Grafana    | http://grafana.demo    |               |
| Prometheus | http://prometheus.demo |               |
| Jaeger     | http://jaeger.demo     |               |

#### habitcentric

| Service  | URL                           | Credentials                                                |
| -------- | ----------------------------- |------------------------------------------------------------|
| UI       | http://habitcentric.demo      | default / default<br/> testing / testing<br/>(or register) |
| Keycloak | http://habitcentric.demo/auth | keycloak / keycloak                                        |

## Uninstall Istio

Make sure to undeploy habitcentric first!

```bash
helmfile destroy
```
