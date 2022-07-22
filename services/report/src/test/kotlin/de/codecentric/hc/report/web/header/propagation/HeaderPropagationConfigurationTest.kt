package de.codecentric.hc.report.web.header.propagation

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.WebApplicationContextRunner
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.AbstractRequestAttributesScope
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.context.annotation.RequestScope as RequestScopeAnnotation

internal class HeaderPropagationConfigurationTest {
    private val contextRunner: WebApplicationContextRunner = WebApplicationContextRunner()
            .withUserConfiguration(HeaderPropagationConfiguration::class.java)

    @Test
    fun `should not provide header propagation rest template customizer bean when spring sleuth is active`() {
        contextRunner
                .withPropertyValues("spring.sleuth.enabled=true")
                .run {
                    assertThat(it).doesNotHaveBean(HeaderPropagationRestTemplateCustomizer::class.java)
                }
    }

    @Test
    fun `should provide header propagation rest template customizer bean when spring sleuth is not active`() {
        contextRunner
                .withPropertyValues("spring.sleuth.enabled=false")
                .run {
                    assertThat(it).hasSingleBean(HeaderPropagationRestTemplateCustomizer::class.java)
                }
    }

    @Test
    fun `should not inject servlet request interceptor when spring sleuth is active`() {
        contextRunner
                .withPropertyValues("spring.sleuth.enabled=true")
                .run {
                    assertThat(it).doesNotHaveBean(WebMvcConfigurer::class.java)
                }
    }

    @Test
    fun `should inject servlet request interceptor when spring sleuth is not active`() {
        contextRunner
                .withPropertyValues("spring.sleuth.enabled=false")
                .run {
                    assertThat(it).hasSingleBean(WebMvcConfigurer::class.java)
                }
    }

    @Test
    fun `should configure request scoped header propagation data when spring sleuth is not active`() {
        contextRunner
                .withPropertyValues("spring.sleuth.enabled=false")
                .run {
                    val requestAttributesScope = mockk<AbstractRequestAttributesScope>(relaxed = true)
                    it.beanFactory.registerScope(WebApplicationContext.SCOPE_REQUEST, requestAttributesScope)

                    val requestScopedBeans = it.getBeansWithAnnotation(RequestScopeAnnotation::class.java)
                    assertThat(requestScopedBeans).containsKey("scopedTarget.headerPropagationData")
                }
    }
}
