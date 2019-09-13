#!/bin/bash
scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

# Create namespace for Istio control plane
echo -n "Creating namespace... "
kubectl create namespace istio-system &> /dev/null
echo "OK"

# Install Istio's Custom Resource Definitions (CRDs)
echo -n "Installing Istio CRDs... "
kubectl apply -f $scriptdir/istio-crds.yaml &> /dev/null
echo "OK"

# Periodically check the CRD installation and continue if done
echo -n "Verifying CRD installation (might take a while)... "
while true
do
    set +e
    result=$(kubectl get crds --ignore-not-found | grep 'istio.io\|certmanager.k8s.io' | wc -l)
    set -e
    if [ $result == '23' ]
    then
        break
    fi
    sleep 5
done
echo "OK"

# Install Istio
echo -n "Installing Istio... "
kubectl apply -f $scriptdir/istio.yaml &> /dev/null
echo "OK"