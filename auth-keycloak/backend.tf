terraform {
  backend "gcs" {
    project = "habitcentric"
    bucket = "keycloak-terraform"
    prefix = "state"
  }
}
