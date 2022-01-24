# 🔌 Docker Compose Deployment for habitcentric

This configuration deploys habitcentric in a minimal configuration and exposes the API Gateway on
Port 9000 of your local machine.
You can use it to get up and running quickly.

## Prerequisites

- Running Docker Daemon
- Properly configured Docker CLI to adminstrate Docker Daemon
- Locally installed Docker Compose

## Quick Start

Start up the Docker Compose stack

```shell
docker-compose up -d
```

Add `habitcentric.demo` to your hosts file using localhost as the IP address (`/etc/hosts` on Linux/macOS,
`%windir%\system32\drivers\etc` on Windows).

```shell
127.0.0.1 habitcentric.demo
```
