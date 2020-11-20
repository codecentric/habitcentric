package de.codecentric.hc.report.web.header.propagation

import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.web.client.RestTemplate

class HeaderPropagationRestTemplateCustomizer(private val headerPropagationData: HeaderPropagationData) : RestTemplateCustomizer {
    override fun customize(restTemplate: RestTemplate?) {
        restTemplate?.interceptors?.add(HeaderPropagationRequestInterceptor(headerPropagationData))
    }
}
