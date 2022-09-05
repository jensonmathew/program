#!/usr/bin/env bash

set +x

# shellcheck source=./tmo-common/shared.sh
. "${TMO_SHARED_SH}"

if ! exists "curl" ; then installPackage "curl"; fi;
if ! exists "jq" ; then installPackage "jq"; fi;
appRespFile="response.json"
helloworldBaseUrl="${DYNAMIC_ENVIRONMENT_URL:-"https://${HELM_APP_NAME}.${K8S_DOMAIN}"}"
deployApiUrl="${helloworldBaseUrl}/api/deploy/info"
totalReq=${TEST_ITERATION:-50}
validationSuccess="false"

tmoLog "K8S_DEPLOY_STRATEGY:- [ ${K8S_DEPLOY_STRATEGY} ]"
tmoLog "DYNAMIC_ENVIRONMENT_URL identified as :- ${DYNAMIC_ENVIRONMENT_URL}" "debug"
tmoLog "deployApiUrl set as:- [ ${deployApiUrl} ]" "debug"

newVerCount=0
oldVerCount=0
failCount=0
successCount=0
newVer="-"
oldVer="-"

for i in $(seq "$totalReq");
do
    echo "-------------------------------------------------------------------------------------------------------------------------"
    tmpMsg="[ ${K8S_DEPLOY_STRATEGY} ]"
    if [ "${K8S_DEPLOY_STRATEGY}" = "BLUE_GREEN" ] && (( i % 2 == 0 )); then
        CANARY_COOKIE_NAME="${CANARY_COOKIE_NAME:-"canary"}"
        tmpMsg="${tmpMsg} ✔ COOKIE, [$CANARY_COOKIE_NAME] "
        status_code=$(curl --max-time 10 -s -H "Content-Type: application/json" -b "$CANARY_COOKIE_NAME=always" -o "$appRespFile" -w "%{http_code}" "$deployApiUrl")
    elif [ "${K8S_DEPLOY_STRATEGY}" = "BLUE_GREEN" ] && (( i % 3 == 0 )); then
        CANARY_HEADER_NAME="${CANARY_HEADER_NAME:-"canary"}"
        CANARY_HEADER_VALUE="${CANARY_HEADER_VALUE:-"always"}"
        tmpMsg="${tmpMsg} ✔ HEADER, [$CANARY_HEADER_NAME] "
        status_code=$(curl --max-time 10 -s -H "Content-Type: application/json" -H "$CANARY_HEADER_NAME: $CANARY_HEADER_VALUE" -o "$appRespFile" -w "%{http_code}" "$deployApiUrl")
    else
        tmpMsg="${tmpMsg} x COOKIE/HEADER"
        status_code=$(curl --max-time 10 -s -H "Content-Type: application/json" -o "$appRespFile" -w "%{http_code}" "$deployApiUrl")
    fi
    if [ "$status_code" != 200 ]; then
        ((failCount++))
        tmoLog "Status Code:- [ $status_code ] observed, Count, [ $failCount ]" "warn"
        continue
    fi
    ((successCount++))
    tmoLogCollapsed "$(cat "${appRespFile}")" "[$i/$totalReq] API RESP: ${tmpMsg}, Url: ${deployApiUrl}"
    deployedVer=$(< "${appRespFile}" jq -r '.deployedVersion')
    pipelineId=$(< "${appRespFile}" jq -r '.pipelineId')
    commitSha=$(< "${appRespFile}" jq -r '.commitSha')
    if [[ "${deployedVer}" == "${APP_VERSION}-${BUILD_NUMBER}" ]] && [[ "${pipelineId//#}" == "${CI_PIPELINE_ID}" ]]
    then
        tmoLog "App RESP, Ver: [ ${deployedVer} ], Pipeline ID: [ ${pipelineId} ], SHA: [ ${commitSha} ]" "success"
        if [ -z "${TEST_ROLLBACK}" ]; then validationSuccess="true"; fi;
        ((newVerCount++))
        newVer=${deployedVer}
    else
        tmoLog "App RESP, Ver: [ ${deployedVer} ], Pipeline ID: [ ${pipelineId} ], SHA: [ ${commitSha} ]" "debug"
        ((oldVerCount++))
        oldVer=${deployedVer}
        if [ "${TEST_ROLLBACK}" = "true" ]; then validationSuccess="true"; fi;
    fi
    sleep 1
done

tmoLog "============================= TEST SUMMARY ==========================================================="
tmoLog "        App Endpoint          :-     [  $deployApiUrl ] " "debug"
tmoLog "        Deploy Strategy       :-     [  $K8S_DEPLOY_STRATEGY ]" "debug"
tmoLog "        Total Requests        :-     [  $totalReq  ] " "debug"
tmoLog "        [ ✔ ] Resp n+1        :-     [  $newVerCount  ], ( ver: $newVer ) " "debug"
tmoLog "        [ ✔ ] Resp, n         :-     [  $oldVerCount  ], ( ver: $oldVer ) " "debug"
tmoLog "        [ ⨯ ] Failed/5xx      :-     [  $failCount  ] " "debug"
tmoLog "======================================================================================================"

if [ "${failCount}"  -ne 0 ]; then 
    tmoLog "Total non 200 responses identified during testing, [ ${failCount} ] " "warn"
fi

if [ "${validationSuccess}" = "false" ]; then
    tmoLog "App Validation Failed " "error"
    exit 1
else
    tmoLog "App Validation Successful " "success"
fi
