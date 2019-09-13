$activity = "Istio installation"
$status = 'Progress:'

# Create namespace for Istio control plane
Write-Progress -Activity $activity -CurrentOperation 'Creating namespace...' -Status $status -PercentComplete 25
kubectl create namespace istio-system | Out-Null

# Install Istio's Custom Resource Definitions (CRDs)
Write-Progress -Activity $activity -CurrentOperation 'Installing Istio CRDs...' -Status $status -PercentComplete 50
kubectl apply -f $PSScriptRoot/istio-crds.yaml | Out-Null

# Periodically check the CRD installation and continue if done
Write-Progress -Activity $activity -CurrentOperation 'Verifying CRD installation (might take a while)...' -Status $status -PercentComplete 75
while ($true) {
    $result = kubectl get crds --ignore-not-found | grep -c 'istio.io\|certmanager.k8s.io'
    if ($result -eq 23) {
        break
    }
    Start-Sleep -s 5
}

# Install Istio
Write-Progress -Activity $activity -CurrentOperation 'Installing Istio...' -Status $status -PercentComplete 100
kubectl apply -f $PSScriptRoot/istio.yaml | Out-Null
