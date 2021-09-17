# Habitcentric

habitcentric is a small demo application which can be used to track habits.

TBD: Overview of services and showcases

# Deployment Environments

habitcentric supports a wide range of deployment environments:

- docker-compose
- kubernetes
    - without service mesh
    - Istio
    - Linkerd
    - Kuma
    - traefik-mesh (does **not** support our current show-case)
- standalone/IDE

## Supported features/components per environment

TBD :)

# Ports

Each habitcentric service runs with the same ports across all deployment options.

| Service           | Port  |
|-------------------|-------|
| gateway           | 9000  |
| habit             | 9001  |
| habit-postgres    | 10001 |
| track             | 9002  |
| track-postgres    | 10002 |
| report            | 9003  |
| ui                | 9004  |
| keycloak          | 8080  |
| keycloak-postgres | 10003 |

# Readmes

## Habitcentric Components

- [auth-keycloak](auth-keycloak/README.md)
- [gateway](gateway/README.md)
- [habbit](habit/README.md)
- [track](track/README.md)
- [ui](ui/README.md)

## Infrastructure

- [istio](infrastructure/istio/README.md)
- [kubernetes](infrastructure/kubernetes/README.md)
- [kuma](infrastructure/kuma/README.md)
- [linkerd](infrastructure/linkerd/README.md)
- [traefik-mesh](infrastructure/traefik-mesh/README.md)

## Additional Tests

- [infra-tests](test/infra/README.md)
- [lpt-locust](test/lpt-locust/README.md)

# GitLab Pipeline

Runs the builds for all services in parallel jobs.

## Relevant Files

- [`.gitlab-ci.yml`](.gitlab-ci.yml): Central pipeline definition. Includes everything.
- [`pipeline/gitlab-jvm.yml`](pipeline/gitlab-jvm.yml): Hidden jobs for jvm test & build
- [`pipeline/gitlab-docker.yml`](pipeline/gitlab-docker.yml): Hidden jobs for docker
- [`habit/.gitlab-ci.yml`](habit/.gitlab-ci.yml): Pipeline for habit service
- [`report/.gitlab-ci.yml`](report/.gitlab-ci.yml): Pipeline for report service
- [`track/.gitlab-ci.yml`](track/.gitlab-ci.yml): Pipeline for track service
- [`ui/.gitlab-ci.yml`](ui/.gitlab-ci.yml): Pipeline for ui
