package de.codecentric.hc.report

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.annotation.RequestScope
import javax.servlet.http.HttpServletRequest

@Configuration
class Configuration {

    @Bean
    @RequestScope
    fun restTemplate(incomingRequest: HttpServletRequest): RestTemplate {
        var restTemplateBuilder = RestTemplateBuilder()
        val authHeader = incomingRequest.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader != null && authHeader.isNotEmpty()) {
            restTemplateBuilder = addJwtInterceptor(restTemplateBuilder, authHeader)
        }
        return restTemplateBuilder.build()
    }

    private fun addJwtInterceptor(
        restTemplateBuilder: RestTemplateBuilder, authHeader: String
    ): RestTemplateBuilder {
        return restTemplateBuilder.additionalInterceptors(
            ClientHttpRequestInterceptor { outgoingRequest: HttpRequest, body: ByteArray?, execution: ClientHttpRequestExecution ->
                outgoingRequest.headers[HttpHeaders.AUTHORIZATION] = authHeader
                execution.execute(outgoingRequest, body!!)
            }
        )
    }
}