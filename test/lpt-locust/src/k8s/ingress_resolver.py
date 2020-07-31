import os

from kubernetes import config, client

from util import Environment


class K8sIngressIpResolver:
    """
    Class to resolve the IP address of a Kubernetes ingress resource using the in-cluster Kubernetes
    API.
    """

    def __init__(self) -> None:
        config.load_incluster_config()

        self.__v1 = client.CoreV1Api()
        self.__networkingV1 = client.NetworkingV1beta1Api()

    def resolve(self) -> str:
        if self.istio_env():
            istio_ingress = self.__v1.read_namespaced_service('istio-ingressgateway', 'istio-system')
            return istio_ingress.spec.cluster_ip
        else:
            gateway_ingress = self.__networkingV1.read_namespaced_ingress('gateway', 'hc-gateway')
            return gateway_ingress.status.load_balancer.ingress[0].ip

    def istio_env(self) -> bool:
        pod_name, namespace_name = Environment.k8s_env_params()
        containers_spec_of_pod = self.__v1.read_namespaced_pod(pod_name, namespace_name).spec.containers
        return any(filter(lambda c: c.name == 'istio-proxy', containers_spec_of_pod))