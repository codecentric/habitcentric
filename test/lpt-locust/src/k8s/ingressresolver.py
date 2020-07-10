import os

from kubernetes import config, client


class K8sIngressIpResolver():

    def __init__(self) -> None:
        env = os.environ.get('ENV')
        if env == 'k8s':
            config.load_incluster_config()
        else:
            config.load_kube_config()

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
        pod_name = os.environ['POD_NAME']
        namespace_name = os.environ['NAMESPACE_NAME']
        containers_spec_of_pod = self.__v1.read_namespaced_pod(pod_name, namespace_name).spec.containers
        return any(filter(lambda c: c.name == 'istio-proxy', containers_spec_of_pod))