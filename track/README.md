# API

## HTTP Endpoints

| Verb   | Endpoint                                                                                 | Description                                                    |
| ------ | ---------------------------------------------------------------------------------------- |--------------------------------------------------------------- |
| GET    | [/track/{userId}/habits/{habitId}](http://localhost:8180/track/{userId}/habits/{habitId) | Retrieves track records for a given habit of a given user      |
| PUT    | [/track/{userId}/habits/{habitId}](http://localhost:8180/track/{userId}/habits/{habitId) | Creates/update track records for a given habit of a given user |

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
./gradlew bootRun
```
