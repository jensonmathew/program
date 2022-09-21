#!/usr/bin/env bash

#set -x

# Exit if any issues
set -e

# shellcheck source=./tmo-common/shared.sh
. "${TMO_SHARED_SH}"

# Fn to validate Canary Deployment
validateAppDeployment(){
    appRespFile="response.json"
    helloworldBaseUrl="http://helloworld.ephemeral.ci"
    deployApiUrl="${helloworldBaseUrl}/api/deploy/info"
    status_code=$(curl --max-time 10 -s -H "Content-Type: application/json" -o "$appRespFile" -w "%{http_code}" "$deployApiUrl")
    tmoLog "httpStatusCode: [ ${status_code} ] for [ ${deployApiUrl} ]" "debug"
    if [ "$status_code" = 200 ]; then
         tmoLogCollapsed "$(cat "${appRespFile}")" "Helloworld Api Response"
         deployedVer=$(< "${appRespFile}" jq -r '.deployedVersion')
         pipelineId=$(< "${appRespFile}" jq -r '.pipelineId')
         commitSha=$(< "${appRespFile}" jq -r '.commitSha')
         tmoLog "App RESP, Ver: [ ${deployedVer} ], Pipeline ID: [ ${pipelineId} ], SHA: [ ${commitSha} ]" "success"
    else
         tmoLog "Helloworld App Deploy API Status [ ${status_code} ]" "error"
    fi
}

# Prepare necessary charts for ephemeral deployment 
cp -r .gitlab-ci/deploy/helloworld/k8s-kind/templates/serviceaccount.yaml "${HELM_CHART_DIR}"/templates/serviceaccount.yaml

echo " -- List of Files in [ ${HELM_CHART_DIR} ] --- "
ls -R "${HELM_CHART_DIR}"
echo " --------------------------------------------- "

# Create Docker Pull Secret
tmoLog "Setting secret for image pull"
kubectl create secret docker-registry "tmobile-cdp-image-pull" --docker-server=registry.gitlab.com --docker-username="${REGISTRY_USERNAME}" --docker-password="${REGISTRY_PASSWORD}" --namespace=ingress-nginx

tmoLog "Modifying host file for host resolution"
tmoLogCollapsed "$(cat /etc/hosts)" "HOSTS FILE"
dockerIp=$(grep "docker " /etc/hosts | grep -oE '((1?[0-9][0-9]?|2[0-4][0-9]|25[0-5])\.){3}(1?[0-9][0-9]?|2[0-4][0-9]|25[0-5])')
tmoLog "dockerIp parsed from /etc/hosts:- $dockerIp"
if [ -z "$dockerIp" ];then tmoLog "dockerIp parsing failed" "warn"; fi;
echo "${dockerIp}       ${HELM_APP_NAME}.${K8S_DOMAIN}" >> /etc/hosts
tmoLogCollapsed "$(cat /etc/hosts)" "MODIFIED HOSTS FILE"

# Jinja parse values yaml
if [ -f "${HELM_VALUES}".j2 ]; then
   genConfigFileFromJ2 "${HELM_VALUES}".j2 "${HELM_VALUES}"
   tmoLogCollapsed "$(cat "${HELM_VALUES}")" "GENERATED VALUES YAML FOR DEPLOYMENT"
fi

# Jinja parse chart yaml
if [ -f "${HELM_CHART_DIR}"/Chart.yaml.j2 ]; then
   genConfigFileFromJ2 "${HELM_CHART_DIR}"/Chart.yaml.j2 "${HELM_CHART_DIR}"/Chart.yaml
fi

# Helm chart installation
tmoLog "Helloworld App Installation initiated" "tmo"
helm upgrade --install "$HELM_APP_NAME" "$HELM_CHART_DIR" --namespace=ingress-nginx --wait

sleep 5
echo "------------------------------------------------------------------------------------------"
helm get manifest "$HELM_APP_NAME" --namespace=ingress-nginx 
echo "------------------------------------------------------------------------------------------"

# Check if the app is available
validateAppDeployment

