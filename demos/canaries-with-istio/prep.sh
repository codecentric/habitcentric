#!/bin/bash
set -eo pipefail

# we want the first element here
# shellcheck disable=SC2128
. "$(dirname "${BASH_SOURCE}")"/../bootstrap-util.sh

required_commands="kubectl helmfile minikube"
# we want parameter expansion here
# shellcheck disable=SC2086
require_commands $required_commands
unset required_commands

bootstrap_k8s "istio" "enable-cni"
istio_install "cni"
habitcentric_deploy "istio"

echo "Done, just a few more steps to run the demo:"
echo "  1. Run minikube tunnel"
echo "  2. Run sudo ./dns.sh or set the habitcentric IP in your hosts file yourself"
echo "  3. Run ./demo.sh"
