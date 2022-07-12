package de.codecentric.hc.report.web.header.propagation

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.annotation.RequestScope
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnProperty("spring.sleuth.enabled", havingValue = "false", matchIfMissing = false)
class HeaderPropagationConfiguration {

    @Bean
    fun restTemplateCustomizerForManualHeaderPropagation(headerPropagationData: HeaderPropagationData): RestTemplateCustomizer =
            HeaderPropagationRestTemplateCustomizer(headerPropagationData)

    @Bean
    @RequestScope
    fun headerPropagationData(): HeaderPropagationData = HeaderPropagationData()

    @Bean
    fun servletRequestHeaderInterceptor(headerPropagationData: HeaderPropagationData): ServletRequestHeaderInterceptor =
            ServletRequestHeaderInterceptor(headerPropagationData)

    @Bean
    fun headerInterceptorWebMvcConfigurer(headerInterceptor: ServletRequestHeaderInterceptor): WebMvcConfigurer =
            object : WebMvcConfigurer {
                override fun addInterceptors(registry: InterceptorRegistry) {
                    registry.addInterceptor(headerInterceptor)
                }
            }
}
