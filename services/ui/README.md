# hc-ui

## Local development

```shell
# start backend service mocks
docker-compose up -d

# install dependencies
yarn install

# start in development mode with hot-reload
yarn run serve
```

You can also start the UI with OIDC enabled:

```shell
# start backend service mocks with additional keycloak
docker-compose -f docker-compose-oidc.yaml up -d

# install dependencies
yarn install

# start in development oidc mode with hot-reload
yarn run serve:oidc
```

### Compile and minify for production
```
yarn run build
```

### Lint and fix files
```
yarn run lint
```

### Run unit tests
```
yarn run test:unit
```

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).
