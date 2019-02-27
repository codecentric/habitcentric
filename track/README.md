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
* PostgreSQL database (port 8221)

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
