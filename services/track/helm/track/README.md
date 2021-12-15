# habitcentric track service

The [habitcentric](https://confluence.codecentric.de/display/HAB/habitcentric) track service is a 
service to track personal habits.

## TL;DR;

```console
$ helm dependency update
$ helm install .
```

## Introduction

This chart bootstraps a [habitcentric track](https://gitlab.com/habitcentric/habitcentric/-/tree/main/services/track) deployment
on a Kubernetes cluster using the Helm package manager.

## Prerequisites

- Kubernetes 1.10+
- Persistent volume provisioner support in the underlying infrastructure

## Installing the Chart
To install the chart with the release name `habitcentric`:

```console
$ helm dependency update
$ helm install --name habitcentric .
```

The command deploys habitcentric track on the Kubernetes cluster in the default configuration.
The [configuration](#configuration) section lists the parameters that can be configured during 
installation.

> **Tip**: List all releases using `helm list`

> **Note**: `helm dependency update` is used to pull the PostgreSQL Helm chart and thus only 
mandatory when `persistence.deployPostgres` is set to `true`.

## Uninstalling the Chart

To uninstall/delete the `habitcentric` deployment:

```console
$ helm delete --purge habitcentric
```

The command removes all the Kubernetes components associated with the chart and deletes the release.

## Configuration

The following table lists the configurable parameters of the habitcentric track chart and their 
default values.

| Parameter                                     | Description                                                                                                            | Default                                                     |
| --------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------- |
| `global.imageRegistry`                        | Global Docker image registry                                                                                           | `nil`                                                       |
| `image.registry`                              | habitcentric track image registry                                                                                      | `docker.io`                                                 |
| `image.repository`                            | habitcentric track image name                                                                                          | `habitcentric/track`                                        |
| `image.tag`                                   | habitcentric track image tag                                                                                           | `latest`                                                    |
| `image.pullPolicy`                            | habitcentric track pull policy                                                                                         | `Always`                                                    |
| `extraEnv`                                    | Extra environment variables for the container                                                                          | `[]`                                                        |
| `service.type`                                | The service type                                                                                                       | `ClusterIP`                                                 |
| `service.port`                                | The service port                                                                                                       | `9002`                                                      |
| `service.nodePort`                            | The node port used if the service is of type `NodePort`                                                                | `nil`                                                       |
| `service.annotations`                         | Annotations for the habitcentric track service                                                                         | `{}`                                                        |
| `persistence.deployPostgres`                  | If `true`, PostgreSQL is deployed alongside the service                                                                | `{}`                                                        |
| `persistence.dbName`                          | The name of the database (if `deployPostgres=false`)                                                                   | `nil`                                                       |
| `persistence.dbHost`                          | The hostname of the database (if `deployPostgres=false`)                                                               | `nil`                                                       |
| `persistence.dbPort`                          | The port of the database (`deployPostgres=false`)                                                                      | `nil`                                                       |
| `persistence.dbUser`                          | The user of the database (`deployPostgres=false`)                                                                      | `nil`                                                       |
| `persistence.dbPassword`                      | The password of the database (`deployPostgres=false`)                                                                  | `nil`                                                       |
| `serviceAccount.enabled`                      | Enable service account (Note: Service Account will only be automatically created if `serviceAccount.name` is not set)  | `false`                                                     |
| `serviceAcccount.name`                        | Name of existing service account                                                                                       | `nil`                                                       |
| `readinessProbe.enabled`                      | If `true`, Kubernetes readiness probe is enabled                                                                       | `true`                                                      |
| `readinessProbe.initialDelaySeconds`          | Delay before liveness probe is initiated                                                                               | 20                                                          |
| `readinessProbe.periodSeconds`                | How often to perform the probe                                                                                         | 120                                                         |
| `readinessProbe.timeoutSeconds`               | When the probe times out                                                                                               | 5                                                           |
| `readinessProbe.failureThreshold`             | Minimum consecutive failures for the probe to be considered failed after having succeeded.                             | 6                                                           |
| `readinessProbe.successThreshold`             | Minimum consecutive successes for the probe to be considered successful after having failed                            | 1                                                           |
| `livenessProbe.enabled`                       | If `true`, Kubernetes liveness probe is enabled                                                                        | `true`                                                      |
| `livenessProbe.initialDelaySeconds`           | Delay before liveness probe is initiated                                                                               | 40                                                          |
| `livenessProbe.periodSeconds`                 | How often to perform the probe                                                                                         | 120                                                         |
| `livenessProbe.timeoutSeconds`                | When the probe times out                                                                                               | 5                                                           |
| `livenessProbe.failureThreshold`              | Minimum consecutive failures for the probe to be considered failed after having succeeded.                             | 6                                                           |
| `livenessProbe.successThreshold`              | Minimum consecutive successes for the probe to be considered successful after having failed                            | 1                                                           |
| `replicas`                                    | Number of gateway instances                                                                                            | 1                                                           |
| `podLabels`                                   | Map of labels to add to the habitcentric track pods                                                                    | `{}`                                                        |
| `podAnnotations`                              | Map of annotations to add to the habitcentric track pods                                                               | `{}`                                                        |
| `updateStrategytype`                          | Update strategy type                                                                                                   | `RollingUpdate`                                             |

Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`.

Alternatively, a YAML file that specifies the values for the parameters can be provided while 
installing the chart. For example,

```console
$ helm install --name my-release -f values.yaml .
```

> **Tip**: You can use the default [values.yaml](values.yaml)

## Persistence
Persistent Volume Claims are used by the PostgreSQL subchart to keep the data across deployments. This is known to work in GCE, AWS, and minikube.
See the [Configuration](#configuration) section to configure the PVC or to disable persistence.
