# Scripted Demos

- [monitoring with istio](monitoring-with-istio)

## Usage

1. Run prep.sh with sudo: `sudo ./prep.sh`
2. Run the demo: `./demo.sh`

## Prerequisites

- bash
- pv
- minikube
- kubectl
- helmfile
- gnu sed (macOS)

## Known Issues

Linux compatibility depends highly on your setup.

The following problems are known:

- linuxbrew causes problems when trying to run binaries installed via brew because linuxbrew uses a
  customized path.
- prep.sh depends on the HOME being preserved by sudo. A workaround is to run the commands manually.
