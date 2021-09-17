# API

## Documentation

This project uses OpenAPI Specification version 2.0 to document its API.

After starting the service locally you can find the documentation here:

* OpenAPI Document: http://localhost:8180/v2/api-docs
* Swagger UI: http://localhost:8180/swagger-ui.html

# Development

## Execute Tests

```
./gradlew test
```

## Build

You can created a fat JAR that contains all dependencies and a Tomcat server to run the application as follows: 

```
./gradlew clean build
```

## Start Application Locally

```
docker-compose up --build
```

It's recommend to use `docker-compose` to start the following components on your local machine:
* [REST webservice](http://localhost:9001/actuator/health)
* PostgreSQL database

`Dockerfile` describes the application's Docker image and expects an existing JAR ([see Build](#build)).
`--build` makes sure that this Docker image is build each time and changes become effective.

SQL scripts in the folder `src/test/resources/db` are executed automatically in alphabetical order to create the database schema and insert some test data.

You can terminate terminate all started containers as follows:

```
docker-compose down
```

It's also possible to start each component separately:

* `docker-compose up db` to start the database
* `docker-compose up app` or `./gradlew bootRun` to start the webservice

## Database
Liquibase is used for continuously migrating the database. 
`src/main/resources/db/changelog/db.changelog-master.yaml` describes the migration steps. 
When starting the application, Spring automatically detects the configuration and applies the migration steps to 
its configured data source if they have not been run yet.

You can use the Liquibase Gradle plugin to conveniently run Liquibase CLI commands. It is especially useful for 
validating and manually applying the change log and rolling back to a previous version.

> Note: You can specify database credentials for the Liquibase Gradle plugin by passing the Gradle properties `dbUrl`, 
`dbUser` and `dbPassword` (i.e. `gradle validate -PdbUrl=<db-url> -PdbUser=<db-user> -PdbPassword=<db-password>`).

Click [here](https://www.liquibase.org/documentation/command_line.html) for an overview of available commands.

> Prerequisite: The database schema `hc_habit` needs to exist because Liquibase is not able to create it on its own and 
store its tables in it.
If necessary, create the schema manually or simply start the application to let the Spring framework do it for you.

## Unit Testing
```bash
./gradlew clean test
```

### Test Code Coverage
The code (not) covered by tests is measured with [JaCoCo](https://github.com/jacoco/jacoco).

You can call the following command to generate a HTML coverage report under `build/reports/jacoco/test/html`
and verify the compliance with coverage rules:
```bash
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

## Integration Testing
```bash
./gradlew clean intTest
```

## Code Format

The [Spotless Gradle plugin](https://github.com/diffplug/spotless/tree/master/plugin-gradle) is used the format the 
source code using [Google Java Format](https://github.com/google/google-java-format). It's recommended to install and 
use the Google Format plugin in your IDE as well.

`gradlew build` will check the code automatically and fail in case of violations.
If necessary, you can fix the code as follows:

```bash
./gradlew spotlessApply
```
