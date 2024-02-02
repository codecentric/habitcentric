import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  id("org.springframework.boot") version "3.2.1"
  id("io.spring.dependency-management") version "1.1.4"
  kotlin("jvm") version "1.9.21"
  kotlin("plugin.spring") version "1.9.21"
  id("com.github.jk1.dependency-license-report") version "2.5"
}

group = "de.codecentric"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

sourceSets {
  create("intTest") {
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
  }
}

val intTestImplementation by configurations.getting {
  extendsFrom(configurations.implementation.get())
}

val intTestRuntimeOnly by configurations.getting {
  extendsFrom(configurations.runtimeOnly.get())
}

tasks.create<Test>("intTest") {
  description = "Runs integration tests."
  group = "verification"

  testClassesDirs = sourceSets.getByName("intTest").output.classesDirs
  classpath = sourceSets.getByName("intTest").runtimeClasspath
  useJUnitPlatform()
  shouldRunAfter(tasks.test)
}

idea {
  module {
    testSources.from(sourceSets["intTest"].kotlin.srcDirs)
  }
}

tasks.check.get().dependsOn(tasks.getByName("intTest"))

repositories {
  mavenCentral()
}

extra["springModulithVersion"] = "1.1.0"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
//  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-websocket")
//  implementation("org.springframework.security:spring-security-messaging")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.flywaydb:flyway-core")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.springframework.kafka:spring-kafka")
  implementation("org.springframework.modulith:spring-modulith-starter-core")
  implementation("org.springframework.modulith:spring-modulith-starter-jdbc")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  runtimeOnly("io.micrometer:micrometer-registry-prometheus")
  runtimeOnly("org.postgresql:postgresql")
  runtimeOnly("org.springframework.modulith:spring-modulith-actuator")
  runtimeOnly("org.springframework.modulith:spring-modulith-observability")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.kafka:spring-kafka-test")
  testImplementation("org.springframework.modulith:spring-modulith-starter-test")
//  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("io.kotest:kotest-assertions-core:5.8.0")
  testImplementation("org.springframework.boot:spring-boot-testcontainers")
  testImplementation("org.testcontainers:junit-jupiter")
  testImplementation("org.testcontainers:kafka")
  testImplementation("org.testcontainers:postgresql")
  intTestImplementation("org.springframework.kafka:spring-kafka-test")
  intTestImplementation("org.springframework.modulith:spring-modulith-starter-test")
  intTestImplementation("org.springframework.boot:spring-boot-starter-test")
//  intTestImplementation("org.springframework.security:spring-security-test")
  intTestImplementation("org.springframework.boot:spring-boot-testcontainers")
  intTestImplementation("org.testcontainers:junit-jupiter")
  intTestImplementation("org.testcontainers:kafka")
  intTestImplementation("org.testcontainers:postgresql")
  intTestImplementation("io.kotest:kotest-assertions-core:5.8.0")

}

dependencyManagement {
  imports {
    mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "21"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
