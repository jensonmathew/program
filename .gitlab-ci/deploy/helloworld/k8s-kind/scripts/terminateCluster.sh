#!/usr/bin/env bash

set +x

# Exit if any issues
set -e

# shellcheck source=./tmo-common/shared.sh
. "${TMO_SHARED_SH}"

tmoLog "KinD cluster Deletion initiated" "debug"
kind delete cluster --name=cdp-hw
