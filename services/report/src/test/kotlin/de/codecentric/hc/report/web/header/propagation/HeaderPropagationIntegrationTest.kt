package de.codecentric.hc.report.web.header.propagation

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import de.codecentric.hc.report.api.model.AchievementRate
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.test.context.ContextConfiguration
import java.net.URI

@SpringBootTest(
        properties = ["spring.sleuth.http.enabled=false"],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [HeaderPropagationIntegrationTest.Initializer::class])
class HeaderPropagationIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    companion object {
        lateinit var wireMockServer: WireMockServer
    }

    class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext?> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            wireMockServer = WireMockServer(WireMockConfiguration().dynamicPort())
            wireMockServer.start()
            val baseUrl = wireMockServer.baseUrl()

            TestPropertyValues
                    .of("habit.service-url=$baseUrl", "track.service-url=$baseUrl")
                    .applyTo(configurableApplicationContext.getEnvironment())
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "Authorization",
        "x-request-id",
        "x-b3-traceid",
        "x-b3-spanid",
        "x-b3-parentspanid",
        "x-b3-sampled",
        "x-b3-flags",
        "x-ot-span-context"
    ])
    fun `should propagate header`(header: String) {
        mockEndpoint("/habits", 200, "[]")

        val httpHeaders = HttpHeaders()
        httpHeaders.put(header, listOf("value"))
        val requestEntity = RequestEntity<AchievementRate>(httpHeaders, HttpMethod.GET, URI.create("/report/achievement"))

        restTemplate.exchange(requestEntity, AchievementRate::class.java)

        wireMockServer.verify(getRequestedFor(urlEqualTo("/habits"))
                .withHeader(header, equalTo("value")))
    }

    private fun mockEndpoint(endpoint: String, statusCode: Int, body: String) {
        wireMockServer.stubFor(get(endpoint)
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withBody(body)
                        .withHeader("Content-Type", "application/json")))
    }
}
