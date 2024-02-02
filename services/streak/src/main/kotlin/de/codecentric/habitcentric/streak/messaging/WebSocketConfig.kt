package de.codecentric.habitcentric.streak.messaging

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.SecurityFilterChain
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
//@EnableWebSocketSecurity
@EnableWebSecurity
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
  override fun registerStompEndpoints(registry: StompEndpointRegistry) {
    // HTTP URL for WebSocket connection
    registry.addEndpoint("/streak-socket").setAllowedOrigins("*")
  }

  override fun configureMessageBroker(registry: MessageBrokerRegistry) {
    // register queue prefix meant for the application
    registry.setApplicationDestinationPrefixes("/app")

    // Configure spring internal broker to use /topic and /queue as prefix
    // There is no special meaning for the broker, it is more like a convention to differentiate
    // /topic as pub-sub and /queue as point to point messaging
    registry.enableSimpleBroker("/topic", "/queue")

    // user specific prefix, this is the default.
    // The UserDestinationHandler maps paths with the /user prefix to user specific destiations
    // For example a subscription on /user/queue/foobar will be (transparently) translated to /queue/foobar-user<sessionId>
    // To send messages to this user the application can send messages via
    // SimpMessagingTemplate.convertAndSendToUser(username, "/queue/foobar")
    // Which will be converted to /user/username/queue/foobar which will be translated to (maybe multiple, if the user has multiple sessions!)
    // /queue/foobar-user<sessionId>. The default is to broadcast a message to all user sessions.
    // registry.setUserDestinationPrefix("/user")
  }

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    return http
      .cors { it.disable() }
      .csrf { it.disable() }
      .authorizeHttpRequests {
        it.requestMatchers("/streak-socket").permitAll()
          .anyRequest().authenticated()
      }
      .build()
  }

  @Configuration
  @Order(Ordered.HIGHEST_PRECEDENCE + 99)
  inner class AuthInterceptorConfigurer : WebSocketMessageBrokerConfigurer {
    override fun configureClientInboundChannel(registration: ChannelRegistration) {
      registration.interceptors(object : ChannelInterceptor {

        override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
          val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
            ?: throw RuntimeException("damn...")

          if (StompCommand.CONNECT == accessor.command) {
            val nativeHeaders: LinkedMultiValueMap<String, Any> =
              message.headers["nativeHeaders"] as LinkedMultiValueMap<String, Any>
            val userName = nativeHeaders["User"]?.get(0)
            if (userName != null && userName is String) {
              val user: Authentication = MyToken(userName)
              accessor.setUser(user)
            }
          }
          return message
        }
      })
    }
  }

  inner class MyToken(private val name: String) :
    AbstractAuthenticationToken(listOf(SimpleGrantedAuthority("ROLE_USER"))) {
    init {
      isAuthenticated = true
    }

    override fun getCredentials(): Any {
      return name
    }

    override fun getPrincipal(): Any {
      return name
    }
  }
}
