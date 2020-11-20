import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.openapi.generator") version "4.2.1"

    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.61"
    kotlin("kapt") version "1.3.72"
    kotlin("plugin.spring") version "1.3.61"
}

group = "de.codecentric.hc"
version = "0.0.1-SNAPSHOT"
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

dependencies {
    implementation("com.squareup.moshi:moshi:1.9.2")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth:2.2.3.RELEASE")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    runtimeOnly("de.codecentric:chaos-monkey-spring-boot:2.2.0")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("com.ninja-squad:springmockk:2.0.1")
    testCompile("com.github.tomakehurst:wiremock-jre8:2.27.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    dependsOn(tasks.openApiGenerate)
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
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
