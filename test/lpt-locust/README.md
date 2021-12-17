# hc-lpt-locust

Load generator for habitcentric based on [Locust](https://locust.io/).

## Requirements

- docker
- docker-compose

## Configuration

| Environment variable          | Description                                                                                         |
|-------------------------------|-----------------------------------------------------------------------------------------------------|
| `ENV`                         | Environment of the load generator. Possible values: `k8s` or `""`                                   |
| `POD_NAME`                    | Pod the load generator is deployed in. Automatically set by k8s.                                    |
| `NAMESPACE_NAME`              | Namespace the load generator pod is deployed in. Must be set manually when `ENV` is set to `k8s`.   |
| `OIDC_ENABLED`                | Determines if OpenID Connect is enabled or not. Possible values: `true` or `false`                  |
| `OIDC_TOKEN_URL`              | OIDC Token URL of identity provider. Can be used to override the default habitcentric OIDC provider |
| `OIDC_CLIENT_ID`              | OIDC Client ID for load generator client. Must be set when OIDC is enabled.                         |
| `OIDC_USERNAME`               | Username of load generator user. Must be set when OIDC is enabled.                                  |
| `OIDC_PASSWORD`               | Password of load generator user. Must be set when OIDC is enabled.                                  |
| `OAUTHLIB_INSECURE_TRANSPORT` | Disables the https check in the python 'oauthlib'                                                   |

The load generator uses the password credentials grant to obtain access and refresh tokens. Tokens
are refreshed automatically.

## How to run

```
export HABITCENTRIC_IP=<habitcentric ingress/gateway IP>
docker-compose up -d --scale worker=3 --build
```

Then visit [http://localhost:8089/](http://localhost:8089) to access locust.
