package de.codecentric.hc.report.web.header.propagation

import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ServletRequestHeaderInterceptorTest {
    private val headerPropagationData = HeaderPropagationData()
    var subject = ServletRequestHeaderInterceptor(headerPropagationData)

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
    fun `given servlet request interceptor receives request, should extract header and write it to header propagation data`(header: String) {
        val request = mockk<HttpServletRequest>()
        every { request.getHeader(any()) } returns "foobar"

        subject.preHandle(request, mockk(), mockk())

        assertThat(headerPropagationData.headers).containsEntry(header, "foobar")
    }
}
