import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import io.spring.gradle.dependencymanagement.dsl.ImportsHandler
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.openapi.generator") version "7.9.0"
    id("com.github.jk1.dependency-license-report") version "2.9"
    id("org.springframework.boot") version "3.1.8"
    id("com.diffplug.spotless") version "6.25.0"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
}

apply(plugin = "io.spring.dependency-management")

apply { from("../gradle/jacoco.gradle") }
apply { from("../gradle/licenses.gradle") }

group = "de.codecentric.hc"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

sourceSets {
    main {
        java {
            srcDir("$rootDir/src/main/kotlin")
            srcDir("$buildDir/generated-sources/src/main/kotlin")
        }
    }
    test {
        java {
            srcDir("$rootDir/src/test/kotlin")
        }
    }
}

repositories {
    mavenCentral()
}

extra["chaosMonkeyVersion"] = "3.1.0"
extra["mockkVersion"] = "1.13.14"
extra["springMockkVersion"] = "4.0.2"
extra["wiremockVersion"] = "3.0.1"
extra["moschiVersion"] = "1.15.2"

dependencies {
    implementation("io.swagger.core.v3:swagger-annotations:2.2.27")
    implementation("com.squareup.moshi:moshi:${property("moschiVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    runtimeOnly("de.codecentric:chaos-monkey-spring-boot:${property("chaosMonkeyVersion")}")
    runtimeOnly("org.aspectj:aspectjweaver")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("io.mockk:mockk:${property("mockkVersion")}")
    testImplementation("com.ninja-squad:springmockk:${property("springMockkVersion")}")
    testImplementation("com.github.tomakehurst:wiremock:${property("wiremockVersion")}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    dependsOn(tasks.openApiGenerate)
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

openApiGenerate {
    inputSpec.set("$rootDir/openapi.yaml")
    generatorName.set("kotlin-spring")
    validateSpec.set(true)
    apiPackage.set("de.codecentric.hc.report.api")
    modelPackage.set("de.codecentric.hc.report.api.model")
    outputDir.set("$buildDir/generated-sources")
    library.set("spring-boot")
    configOptions.put("basePackage", "de.codecentric.hc.report")
    configOptions.put("interfaceOnly", "true")
    configOptions.put("useSpringBoot3", "true")
    configOptions.put("documentationProvider", "none")
}

tasks.named("openApiGenerate") {
    doLast {
        // OpenAPI generator plugin for Kotlin Spring generates a useless application class
        File("$buildDir/generated-sources/src/main/kotlin/de/codecentric/hc/report/Application.kt").delete()
    }
}

// disable the jar task to prevent the plain jar from being created.
tasks.named<Jar>("jar") {
  enabled = false
}
