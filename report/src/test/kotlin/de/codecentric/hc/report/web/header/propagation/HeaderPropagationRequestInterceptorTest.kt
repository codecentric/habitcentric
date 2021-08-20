package de.codecentric.hc.report.web.header.propagation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution

internal class HeaderPropagationRequestInterceptorTest {
    @Test
    fun `should set all headers in the outgoing request`() {
        val requestMock = mockk<HttpRequest>()
        val clientHttpRequestExecution = mockk<ClientHttpRequestExecution>(relaxed = true)
        every { requestMock.headers } returns HttpHeaders()

        val subject = HeaderPropagationRequestInterceptor(HeaderPropagationData(mapOf(
                "test" to "foo"
        )))
        subject.intercept(requestMock, ByteArray(0), clientHttpRequestExecution)

        assertThat(requestMock.headers).contains(entry("test", listOf("foo")))
        verify { clientHttpRequestExecution.execute(any(), any()) }
    }
}
