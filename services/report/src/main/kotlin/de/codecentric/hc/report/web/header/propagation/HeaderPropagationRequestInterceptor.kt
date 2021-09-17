package de.codecentric.hc.report.web.header.propagation

import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class HeaderPropagationRequestInterceptor(private val headerPropagationData: HeaderPropagationData) : ClientHttpRequestInterceptor {
    override fun intercept(outgoingRequest: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        headerPropagationData.headers?.forEach { outgoingRequest.headers.add(it.key, it.value) }
        return execution.execute(outgoingRequest, body)
    }
}
