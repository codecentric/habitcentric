import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import io.spring.gradle.dependencymanagement.dsl.ImportsHandler
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.openapi.generator") version "5.4.0"
    id("com.github.jk1.dependency-license-report") version "2.1"
    id("org.springframework.boot") version "2.7.6"
    id("com.diffplug.spotless") version "6.12.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    // we should switch to the plugin but enabling it causes warnings for the openapi generator
    //id("io.freefair.lombok") version "6.2.0"
}

apply(plugin = "io.spring.dependency-management")

apply { from("../gradle/jacoco.gradle") }
apply { from("../gradle/licenses.gradle") }

group = "de.codecentric.hc"
java.sourceCompatibility = JavaVersion.VERSION_11

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

extra["chaosMonkeyVersion"] = "2.7.0"
extra["mockkVersion"] = "1.13.3"
extra["springMockkVersion"] = "2.0.3"
extra["wiremockVersion"] = "2.27.2"
extra["moschiVersion"] = "1.14.0"
extra["springCloudVersion"] = "2021.0.3"

dependencies {
    implementation("com.squareup.moshi:moshi:${property("moschiVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}"))
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    runtimeOnly("de.codecentric:chaos-monkey-spring-boot:${property("chaosMonkeyVersion")}")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
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
        jvmTarget = "11"
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
}

tasks.named("openApiGenerate") {
    doLast {
        // OpenAPI generator plugin for Kotlin Spring generates a useless application class
        File("$buildDir/generated-sources/src/main/kotlin/de/codecentric/hc/report/Application.kt").delete()
    }
}
