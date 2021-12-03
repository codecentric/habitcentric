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

- [auth-keycloak](services/auth-keycloak/README.md)
- [gateway](services/gateway/README.md)
- [habbit](hservices/abit/README.md)
- [track](services/track/README.md)
- [ui](services/ui/README.md)

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

# Known Issues

## docker & minikube with kvm2 driver

The network minikube uses for kvm2 VMs restricts routing to the guest. This prevents communication
from docker containers to the minikube VM.

When you want to use the locust load & performance testing running inside docker containers on the
host you need to apply the following workaround.

Check which bridge interface is used by the network

```shell
sudo virsh net-info minikube-net
```

```
sudo virsh net-info minikube-net
Name:           minikube-net
UUID:           b006294e-dcbd-4263-b9e0-af9cd3d88ba1
Active:         yes
Persistent:     yes
Autostart:      yes
Bridge:         virbr1
```

To take a look at the rules run `sudo iptables -vnxL FORWARD`
The rules you don't want are looking like this:

```
Chain FORWARD (policy DROP 0 packets, 0 bytes)
    pkts      bytes target     prot opt in     out     source               destination
       0        0 REJECT     all  --  *      virbr1  0.0.0.0/0            0.0.0.0/0            reject-with icmp-port-unreachable
       0        0 REJECT     all  --  virbr1 *       0.0.0.0/0            0.0.0.0/0            reject-with icmp-port-unreachable
```

To remove the rules run:

```shell
sudo iptables -D FORWARD -i virbr1 -s 0.0.0.0/0 -d 0.0.0.0/0 -j REJECT
sudo iptables -D FORWARD -o virbr1 -s 0.0.0.0/0 -d 0.0.0.0/0 -j REJECT
```

It is possible that there are multiple identical iptables rules, just run the delete commands until
iptables returns an error:
`iptables: Bad rule (does a matching rule exist in that chain?).`
