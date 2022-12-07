#!/usr/bin/env bash

#set -x

# Exit if any issues
set -e

# shellcheck source=./tmo-common/shared.sh
. "${TMO_SHARED_SH}"

if ! exists "curl" ; then installPackage "curl"; fi;

# Install kubectl
tmoLog "Installing kubectl" "debug"
curl --progress-bar -LO https://storage.googleapis.com/kubernetes-release/release/"$KUBECTL_VERSION"/bin/linux/amd64/kubectl
chmod +x ./kubectl
mv ./kubectl /usr/local/bin/kubectl
tmoLog "Installed kubectl             :   [ $(kubectl version --short) ]" "tmo"

# Install KinD
tmoLog "Installing kind" "debug"
curl --progress-bar -Lo ./kind https://kind.sigs.k8s.io/dl/"$KIND_VERSION"/kind-linux-amd64
chmod +x ./kind
mv ./kind /usr/local/bin/kind
tmoLog "Installed kind                :   [ $(kind version) ]" "tmo"

# Install Helm
tmoLog "Installing helm"
curl --progress-bar -L https://get.helm.sh/helm-"$HELM_VERSION"-linux-amd64.tar.gz | tar -xz
chmod +x ./linux-amd64/helm
mv ./linux-amd64/helm /usr/local/bin/helm
tmoLog "Installed helm                :   [ $(helm version --short) ]" "tmo"

# Cluster creation
tmoLog "Initiating KinD cluster"
kind create cluster --name=cdp-hw --config=.gitlab-ci/deploy/helloworld/k8s-kind/config/kind-gitlab.yaml --wait 180s
sed -i -E -e 's/localhost|0\.0\.0\.0/docker/g' "$HOME/.kube/config"
tmoLog "KinD installation complete!!!"

# Modify cluster to disable cert checking
kubectl config set-cluster kind-cdp-hw --insecure-skip-tls-verify=true

# Install IC
tmoLog "Creating NameSpace and installing Nginx ingress controller"
kubectl apply -f .gitlab-ci/deploy/helloworld/k8s-kind/config/ingress-controller.yaml

# Remove the Validating Webhook entirely:
# REF: https://stackoverflow.com/questions/61616203/nginx-ingress-controller-failed-calling-webhook
kubectl delete -A ValidatingWebhookConfiguration ingress-nginx-admission

# Configure context to use newly created NS
kubectl config set-context kind-cdp-hw --namespace=ingress-nginx

