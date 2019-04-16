# Kubernetes Configuration

## Deployment
Prerequisites:
- Running Kubernetes cluster
- Properly configured Kubernetes client `kubectl` to administrate your cluster

> Note: The configuration was tested on Google Kubernetes Engine (GKE) and Minikube

Create a `Namespace` for habitcentric if it does not exist yet:
```bash
kubectl create namespace habitcentric
```

Deploy the service:
```bash
kubectl apply -f k8s/
```

If you are done, you can undeploy the UI service with the following command:
```bash
kubectl delete -f k8s/
```