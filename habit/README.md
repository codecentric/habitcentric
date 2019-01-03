# API

## HTTP Endpoints

| Verb   | Endpoint                                                  | Description             |
| ------ | --------------------------------------------------------- |------------------------ |
| GET    | [/habits](http://localhost:8180/habits)                   | Retrieves all habits    |
| POST   | [/habits/{id}](http://localhost:8180/habits/{id})         | Creates new habits      |
| DELETE | [/habits/{id}](http://localhost:8180/habits/{id})         | Deletes existing habits |
| GET    | [/actuator/health](http://localhost:8180/actuator/health) | Health check            |

# Development

## Execute Tests

```
./gradlew test
```

## Start Application Locally

### Start Database

```
docker-compose up
```

This starts PostgreSQL on port 5432 and a [database administration UI on port 8380](http://localhost:8380/).

SQL scripts in the folder `src/test/resources/db` are executed automatically in alphabetical order to create the database schema and insert some test data.

### Start Webservice

Execute the following command to start the application on port 8180:

```
./gradlew bootRun
```
