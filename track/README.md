# API

## HTTP Endpoints

| Verb   | Endpoint                         | Description                                                    |
| ------ | -------------------------------- |--------------------------------------------------------------- |
| GET    | /track/{userId}/habits/{habitId} | Retrieves track records for a given habit of a given user      |
| PUT    | /track/{userId}/habits/{habitId} | Creates/update track records for a given habit of a given user |

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
* [REST webservice](http://localhost:8219/actuator/health) (port 8219)
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
* `docker-compose up app` or `./gradlew bootRun` to start the webservice (Gradle will start the service on port 8209)

## Database
Flyway is used for continuously migrating the database.
The folder `src/main/resources/db/migration` contains SQL migration scripts. Every migration must comply a specific 
[naming pattern](https://flywaydb.org/documentation/migrations#naming) to be picked up by Flyway.
When starting the application, Spring automatically detects the SQL scripts and applies the migration steps to its 
configured data source if they have not been run yet.

You can use the Flyway Gradle plugin to conveniently run Flyway CLI commands. It is especially useful for printing 
status information about migrations, validating applied migrations against the ones available on the classpath and 
manually migrating the database.

> Note: You can specify database credentials for the Flyway Gradle plugin by passing the Gradle properties `dbUrl`, 
`dbUser` and `dbPassword` (i.e. `gradle flywayInfo -PdbUrl=<db-url> -PdbUser=<db-user> -PdbPassword=<db-password>`).

Click [here](https://flywaydb.org/documentation/commandline/#commands) for an overview of available commands.

## Unit Testing
```
./gradlew clean test
```

### Test Code Coverage
The code (not) covered by tests is measured with [JaCoCo](https://github.com/jacoco/jacoco).

You can call the following command to generate a HTML coverage report under `build/reports/jacoco/test/html`
and verify the compliance with coverage rules:
```
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

## Integration Testing
```
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
