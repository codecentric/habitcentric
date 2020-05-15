# habitcentric gateway service

The [habitcentric](https://confluence.codecentric.de/display/HAB/habitcentric) gateway service 
is an API gateway that routes incoming requests to the corresponding habitcentric services

## TL;DR;

```console
$ helm install .
```

## Introduction

This chart bootstraps a [habitcentric gateway](https://gitlab.com/habitcentric/hc-gateway) deployment
on a Kubernetes cluster using the Helm package manager.

## Prerequisites

- Kubernetes 1.10+

## Installing the Chart

To install the chart with the release name `habitcentric`:

```console
$ helm install --name habitcentric .
```

The command deploys habitcentric gateway on the Kubernetes cluster in the default configuration.
The [configuration](#configuration) section lists the parameters that can be configured during 
installation.

> **Tip**: List all releases using `helm list`

## Uninstalling the Chart

To uninstall/delete the `habitcentric` deployment:

```console
$ helm delete --purge habitcentric
```

The command removes all the Kubernetes components associated with the chart and deletes the release.

## Configuration

The following table lists the configurable parameters of the habitcentric gateway chart and their 
default values.

| Parameter                                     | Description                                                                                                                               | Default                                                     |
| --------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------- |
| `global.imageRegistry`                        | Global Docker image registry                                                                                                              | `nil`                                                       |
| `image.registry`                              | habitcentric gateway image registry                                                                                                       | `docker.io`                                                 |
| `image.repository`                            | habitcentric gateway image name                                                                                                           | `habitcentric/gateway`                                      |
| `image.tag`                                   | habitcentric gateway image tag                                                                                                            | `latest`                                                    |
| `image.pullPolicy`                            | habitcentric gateway pull policy                                                                                                          | `Always`                                                    |
| `service.type`                                | The service type                                                                                                                          | `NodePort`                                                  |
| `service.port`                                | The service port                                                                                                                          | `8419`                                                      |
| `service.nodePort`                            | The node port used if the service is of type `NodePort`                                                                                   | `nil`                                                       |
| `service.annotations`                         | Annotations for the habitcentric gateway service                                                                                          | `{}`                                                        |
| `serviceAccount.enabled`                      | Enable service account (Note: Service Account will only be automatically created if `serviceAccount.name` is not set)                     | `false`                                                     |
| `serviceAcccount.name`                        | Name of existing service account                                                                                                          | `nil`                                                       |
| `readinessProbe.enabled`                      | If `true`, Kubernetes readiness probe is enabled                                                                                          | `true`                                                      |
| `readinessProbe.initialDelaySeconds`          | Delay before liveness probe is initiated                                                                                                  | 20                                                          |
| `readinessProbe.periodSeconds`                | How often to perform the probe                                                                                                            | 120                                                         |
| `readinessProbe.timeoutSeconds`               | When the probe times out                                                                                                                  | 5                                                           |
| `readinessProbe.failureThreshold`             | Minimum consecutive failures for the probe to be considered failed after having succeeded.                                                | 6                                                           |
| `readinessProbe.successThreshold`             | Minimum consecutive successes for the probe to be considered successful after having failed                                               | 1                                                           |
| `livenessProbe.enabled`                       | If `true`, Kubernetes liveness probe is enabled                                                                                           | `true`                                                      |
| `livenessProbe.initialDelaySeconds`           | Delay before liveness probe is initiated                                                                                                  | 40                                                          |
| `livenessProbe.periodSeconds`                 | How often to perform the probe                                                                                                            | 120                                                         |
| `livenessProbe.timeoutSeconds`                | When the probe times out                                                                                                                  | 5                                                           |
| `livenessProbe.failureThreshold`              | Minimum consecutive failures for the probe to be considered failed after having succeeded.                                                | 6                                                           |
| `livenessProbe.successThreshold`              | Minimum consecutive successes for the probe to be considered successful after having failed                                               | 1                                                           |
| `ingress.enabled`                             | If `true`, an ingress is created                                                                                                          | `false`                                                     |
| `ingress.annotations`                         | Annotations for the ingress                                                                                                               | `{}`                                                        |
| `ingress.labels`                              | Additional labels for the Keycloak ingress                                                                                                | `{}`                                                        |
| `ingress.path`                                | Path for the ingress                                                                                                                      | `/`                                                         |
| `ingress.hosts`                               | A list of ingress hosts                                                                                                                   | `[habitcentric.demo]`                                       |
| `ingress.tls`                                 | A list of [IngressTLS](https://v1-9.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.9/#ingresstls-v1beta1-extensions) items | `[]`                                                        |
| `habitUri`                                    | URI of habit service                                                                                                                      | `http://habitcentric-habit:8080`                            |
| `trackUri`                                    | URI of track service                                                                                                                      | `http://habitcentric-track:8080`                            |
| `reportUri`                                   | URI of report service                                                                                                                     | `http://habitcentric-report:8080`                           |
| `uiUri`                                       | URI of gateway service                                                                                                                    | `http://habitcentric-ui`                                    |
| `replicas`                                    | Number of gateway instances                                                                                                               | 1                                                           |
| `podLabels`                                   | Map of labels to add to the habitcentric gateway pods                                                                                     | `{}`                                                        |
| `podAnnotations`                              | Map of annotations to add to the habitcentric gateway pods                                                                                | `{}`                                                        |
| `updateStrategyType`                          | Update strategy type                                                                                                                      | `RollingUpdate`                                             |

Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`.

Alternatively, a YAML file that specifies the values for the parameters can be provided while 
installing the chart. For example,

```console
$ helm install --name my-release -f values.yaml .
```

> **Tip**: You can use the default [values.yaml](values.yaml)
