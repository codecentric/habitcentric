# hc-lpt-locust

Load generator for habitcentric

## Configuration

| Environment variable | Description                                                                                       |
| -------------------- | ------------------------------------------------------------------------------------------------- |
| `ENV`                | Environment of the load generator. Possible values: `k8s` or `""`                                 |
| `POD_NAME`           | Pod the load generator is deployed in. Automatically set by k8s.                                  |
| `NAMESPACE_NAME`     | Namespace the load generator pod is deployed in. Must be set manually when `ENV` is set to `k8s`. |
| `OIDC_ENABLED`       | Determines if OpenID Connect is enabled or not. Possible values: `true` or `false`                |
| `OIDC_TOKEN_URL`     | OIDC Token URL of identity provider. Must be set when OIDC is enabled.                            |
| `OIDC_CLIENT_ID`     | OIDC Client ID for load generator client. Must be set when OIDC is enabled.                       |
| `OIDC_USERNAME`      | Username of load generator user. Must be set when OIDC is enabled.                                |
| `OIDC_PASSWORD`      | Password of load generator user. Must be set when OIDC is enabled.                                |

The load generator uses the password credentials grant to obtain access and refresh tokens.
Tokens are refreshed automatically.

## How to run

```
export HABITCENTRIC_IP=<habitcentric ingress/gateway IP>
docker-compose up -d --scale workers=3 --build
```
