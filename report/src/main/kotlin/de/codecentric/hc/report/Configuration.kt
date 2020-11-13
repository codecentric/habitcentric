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

        val requestIdHeader = incomingRequest.getHeader("x-request-id")
        val traceIdHeader = incomingRequest.getHeader("x-b3-traceid")
        val spanIdHeader = incomingRequest.getHeader("x-b3-spanid")
        val parentSpanIdHeader = incomingRequest.getHeader("x-b3-parentspanid")
        val sampledHeader = incomingRequest.getHeader("x-b3-sampled")
        val flagsHeader = incomingRequest.getHeader("x-b3-flags")
        val spanContextHeader = incomingRequest.getHeader("x-ot-span-context")

        val map = mapOf(
                "x-request-id" to requestIdHeader,
                "x-b3-traceid" to traceIdHeader,
                "x-b3-spanid" to spanIdHeader,
                "x-b3-parentspanid" to parentSpanIdHeader,
                "x-b3-sampled" to sampledHeader,
                "x-b3-flags" to flagsHeader,
                "x-ot-span-context" to spanContextHeader
        )

        restTemplateBuilder = addTraceInterceptor(restTemplateBuilder, map)

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

    private fun addTraceInterceptor(
            restTemplateBuilder: RestTemplateBuilder, traceHeaders: Map<String, String>
    ): RestTemplateBuilder {
        return restTemplateBuilder.additionalInterceptors(
                ClientHttpRequestInterceptor { outgoingRequest: HttpRequest, body: ByteArray?, execution: ClientHttpRequestExecution ->
                    outgoingRequest.headers["x-request-id"] = traceHeaders.get("x-request-id")
                    outgoingRequest.headers["x-b3-traceid"] = traceHeaders.get("x-b3-traceid")
                    outgoingRequest.headers["x-b3-spanid"] = traceHeaders.get("x-b3-spanid")
                    outgoingRequest.headers["x-b3-parentspanid"] = traceHeaders.get("x-b3-parentspanid")
                    outgoingRequest.headers["x-b3-sampled"] = traceHeaders.get("x-b3-sampled")
                    outgoingRequest.headers["x-b3-flags"] = traceHeaders.get("x-b3-flags")
                    outgoingRequest.headers["x-ot-span-context"] = traceHeaders.get("x-ot-span-context")
                    execution.execute(outgoingRequest, body!!)
                }
        )
    }
}