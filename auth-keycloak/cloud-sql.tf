resource "google_sql_database_instance" "master" {
  # SQL instance names cannot be reused within a week.
  # That's why this resource should not be destroyed on a regular basis.
  name = "keycloak-master-instance"
  database_version = "POSTGRES_9_6"
  region = "europe-west3"
  settings {
    tier = "db-f1-micro"
  }
}

resource "google_sql_database" "keycloak" {
  instance = "${google_sql_database_instance.master.name}"
  name = "keycloak"
  charset = "UTF8"
  collation = "en_US.UTF8"
}

resource "google_sql_user" "users" {
  instance = "${google_sql_database_instance.master.name}"
  name = "keycloak"
  password = "password"
}
