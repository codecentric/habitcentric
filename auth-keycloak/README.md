# auth-keycloak

## Start Keycloak locally

```bash
docker-compose up
```

For local testing of Keycloak, it's recommended to use `docker-compose` to start the following components on your local machine:
* Keycloak (port 8080)
* PostgreSQL database

You can terminate all started containers as follows:

```bash
docker-compose down
```

The Postgres database is persisted using a Docker volume. If you want to delete your existing Keycloak configuration to start from scratch run:

```bash
docker volume rm auth-keycloak_postgres-data
```

> Make sure that Keycloak is not running when deleting the volume.

## Infrastructure
The required infrastructure (e.g. the database) in the Google Cloud is managed with Terraform.
Please refer to [terraform.md](terraform.md) for more details.
