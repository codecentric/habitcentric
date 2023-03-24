package de.codecentric.hc.report.web.header.propagation

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class ServletRequestHeaderInterceptor(val headerPropagationData: HeaderPropagationData) : HandlerInterceptor {
    val interceptHeaders = listOf(
            "Authorization",
            "x-request-id",
            "x-b3-traceid",
            "x-b3-spanid",
            "x-b3-parentspanid",
            "x-b3-sampled",
            "x-b3-flags",
            "x-ot-span-context"
    )

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val extractedHeaders = mutableMapOf<String, String>()
        interceptHeaders.forEach {
            val headerValue = request.getHeader(it)
            if (headerValue != null) {
                extractedHeaders[it] = headerValue
            }
        }

        headerPropagationData.headers = extractedHeaders
        return super.preHandle(request, response, handler)
    }
}
