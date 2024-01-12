package de.codecentric.habitcentric

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestStreakApplication {

  @Bean
  @ServiceConnection
  fun kafkaContainer(): KafkaContainer {
    return KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
  }

  @Bean
  @ServiceConnection
  fun postgresContainer(): PostgreSQLContainer<*> {
    return PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
  }

  @Bean
  @ServiceConnection(name = "openzipkin/zipkin")
  fun zipkinContainer(): GenericContainer<*> {
    return GenericContainer(DockerImageName.parse("openzipkin/zipkin:latest")).withExposedPorts(9411)
  }

}

fun main(args: Array<String>) {
  fromApplication<StreakApplication>().with(TestStreakApplication::class).run(*args)
}
