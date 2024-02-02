package de.codecentric.habitcentric.streak.messaging

import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import java.security.Principal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Controller
class WebSocketController(
  val simpMessagingTemplate: SimpMessagingTemplate,
  val simpUserRegistry: SimpUserRegistry
) {

  @MessageMapping("/helloApp")
  @SendTo("/topic/hello")
  fun appMessage(message: Message<String>): String {
    return message.payload
  }

  @SubscribeMapping("/ticker")
  fun sub(): String {
    return LocalDateTime.now().format(DateTimeFormatter.ISO_TIME);
  }


  @Scheduled(fixedDelay = 10000)
  fun emitTime() {
    val time = LocalDateTime.now().format(DateTimeFormatter.ISO_TIME)
    // global broadcast
    simpMessagingTemplate.convertAndSend("/topic/ticker", time)

    // per connected user message
    simpUserRegistry.users.forEach {
      val name = it.name
      simpMessagingTemplate.convertAndSendToUser(
        name,
        "/queue/streak-updates",
        "hi ${name}, its $time"
      );
    }
  }


  // How can we react to a new subscription?
  @SubscribeMapping("/streak-updates")
  @SendToUser("/queue/streak-updates")
  fun userSub(principal: Principal): String {
    return "Welcome ${principal.name}!"
  }
}
