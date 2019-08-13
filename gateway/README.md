# Development

## Execute Tests

```
./gradlew test
```

### Test Code Coverage
The code (not) covered by tests is measured with [JaCoCo](https://github.com/jacoco/jacoco).

You can call the following command to generate a HTML coverage report under `build/reports/jacoco/test/html`
and verify the compliance with coverage rules:
```
./gradlew test jacocoTestReport jacocoTestCoverageVerification
```

## Build

You can created a fat JAR that contains all dependencies and a Tomcat server to run the application as follows: 

```
./gradlew clean build
```

## Start Application Locally

To start the gateway on port 8431 with Gradle execute:
```
./gradlew bootRun
```

It's recommend to use `docker-compose` to start the gateway on port 8419:
```
docker-compose up --build
```

To enable the login via Keycloak, an entry needs to be added in `/etc/hosts` (C:\Windows\System32\Drivers\etc\hosts on Windows).
```
127.0.0.1   habitcentric.demo
```

`Dockerfile` describes the application's Docker image and expects an existing JAR ([see Build](#build)).
`--build` makes sure that this Docker image is build each time and changes become effective.

You can terminate all started containers as follows:

```
docker-compose down
```
