# ⚙️ Scripted Demo: Canary Releasing with Istio

```shell
# prepare minikube cluster
./prep.sh

# activate minikube tunnel for external IPS
minikube tunnel

# prepare DNS resolution for habitcentric.demo
sudo ./dns.sh

# execute interactive demo
./demo.sh
```
