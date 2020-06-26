# habitcentric habit service

The [habitcentric](https://confluence.codecentric.de/display/HAB/habitcentric) report service is a 
service to generate reports about personal habit progress.

## TL;DR;

```console
$ helm dependency update
$ helm install .
```

## Introduction

This chart bootstraps a [habitcentric report](https://gitlab.com/habitcentric-playground/hc-report) deployment
on a Kubernetes cluster using the Helm package manager.

## Prerequisites

- Kubernetes 1.10+

## Installing the Chart
To install the chart with the release name `habitcentric-report`:

```console
$ helm dependency update
$ helm install --name habitcentric-report .
```

The command deploys habitcentric report on the Kubernetes cluster in the default configuration.
The [configuration](#configuration) section lists the parameters that can be configured during 
installation.

> **Tip**: List all releases using `helm list`

## Uninstalling the Chart

To uninstall/delete the `habitcentric` deployment:

```console
$ helm delete --purge habitcentric-report
```

The command removes all the Kubernetes components associated with the chart and deletes the release.

## Configuration

The following table lists the configurable parameters of the habitcentric report chart and their 
default values.

| Parameter                                     | Description                                                                                                            | Default                                                     |
| --------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------- |
| `global.imageRegistry`                        | Global Docker image registry                                                                                           | `nil`                                                       |
| `image.registry`                              | habitcentric report image registry                                                                                     | `docker.io`                                                 |
| `image.repository`                            | habitcentric report image name                                                                                         | `habitcentric/habit`                                        |
| `image.tag`                                   | habitcentric report image tag                                                                                          | `latest`                                                    |
| `image.pullPolicy`                            | habitcentric report pull policy                                                                                        | `Always`                                                    |
| `service.type`                                | The service type                                                                                                       | `ClusterIP`                                                 |
| `service.port`                                | The service port                                                                                                       | `8080`                                                      |
| `service.nodePort`                            | The node port used if the service is of type `NodePort`                                                                | `nil`                                                       |
| `service.annotations`                         | Annotations for the habitcentric report service                                                                        | `{}`                                                        |
| `serviceAccount.enabled`                      | Enable service account (Note: Service Account will only be automatically created if `serviceAccount.name` is not set)  | `false`                                                     |
| `serviceAcccount.name`                        | Name of existing service account                                                                                       | `nil`                                                       |
| `readinessProbe.enabled`                      | If `true`, Kubernetes readiness probe is enabled                                                                       | `true`                                                      |
| `readinessProbe.initialDelaySeconds`          | Delay before readiness probe is initiated                                                                              | 20                                                          |
| `readinessProbe.periodSeconds`                | How often to perform the probe                                                                                         | 120                                                         |
| `readinessProbe.timeoutSeconds`               | When the probe times out                                                                                               | 5                                                           |
| `readinessProbe.failureThreshold`             | Minimum consecutive failures for the probe to be considered failed                                                     | 6                                                           |
| `readinessProbe.successThreshold`             | Minimum consecutive successes for the probe to be considered successful                                                | 1                                                           |
| `livenessProbe.enabled`                       | If `true`, Kubernetes liveness probe is enabled                                                                        | `true`                                                      |
| `livenessProbe.initialDelaySeconds`           | Delay before liveness probe is initiated                                                                               | 40                                                          |
| `livenessProbe.periodSeconds`                 | How often to perform the probe                                                                                         | 120                                                         |
| `livenessProbe.timeoutSeconds`                | When the probe times out                                                                                               | 5                                                           |
| `livenessProbe.failureThreshold`              | Minimum consecutive failures for the probe to be considered failed                                                     | 6                                                           |
| `livenessProbe.successThreshold`              | Minimum consecutive successes for the probe to be considered successful                                                | 1                                                           |
| `replicas`                                    | Number of report instances                                                                                             | 1                                                           |
| `podLabels`                                   | Map of labels to add to the habitcentric report pods                                                                   | `{}`                                                        |
| `podAnnotations`                              | Map of annotations to add to the habitcentric report pods                                                              | `{}`                                                        |
| `updateStrategyType`                          | Update strategy type                                                                                                   | `RollingUpdate`                                             |

Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`.

Alternatively, a YAML file that specifies the values for the parameters can be provided while 
installing the chart. For example,

```console
$ helm install --name habitcentric-report -f values.yaml .
```

> **Tip**: You can use the default [values.yaml](values.yaml)
