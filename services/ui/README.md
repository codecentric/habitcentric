# hc-ui

## Project setup
```
yarn install
```

### Compiles and hot-reloads for development
```
yarn run serve
```

To start the required dependencies (habit, track, report) with wiremock run:

```
docker-compose up habit report track
```

### Compiles and minifies for production
```
yarn run build
```

### Lints and fixes files
```
yarn run lint
```

### Run your unit tests
```
yarn run test:unit
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

## Docker Compose
The UI can be started locally as follows:
```
docker-compose up --build
```
WireMock is started in the background to stub backend requests.

Use the following command to stop the containers again.
```
docker-compose down
```
