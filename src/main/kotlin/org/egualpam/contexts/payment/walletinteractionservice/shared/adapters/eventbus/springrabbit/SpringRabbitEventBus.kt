package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.springrabbit

import com.fasterxml.jackson.databind.ObjectMapper
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.shared.PublicEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.shared.PublicEventData
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.shared.WalletCreatedPublicEventData
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletCreated
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate

class SpringRabbitEventBus(
  private val objectMapper: ObjectMapper,
  private val rabbitStreamTemplate: RabbitStreamTemplate
) : EventBus {

  private val logger: Logger = LoggerFactory.getLogger(this::class.java)

  override fun publish(domainEvents: Set<DomainEvent>) {
    domainEvents
        .map {
          PublicEvent(
              id = it.id(),
              type = typeFrom(it),
              data = contentFrom(it),
          )
        }
        .forEach {
          val messageData = objectMapper.writeValueAsBytes(it)

          val message = rabbitStreamTemplate.messageBuilder()
              .addData(messageData)
              .build()

          rabbitStreamTemplate.send(message)
          logger.info("Event [${it.type}] has been published")
        }
  }

  // TODO: Extend and refactor once other events are also published
  private fun typeFrom(domainEvent: DomainEvent): String {
    if (WalletCreated::class.java.isInstance(domainEvent)) {
      return "payment.wallet.created"
    }

    // TODO: Use custom exception
    throw RuntimeException("Domain event not supported")
  }

  private fun contentFrom(domainEvent: DomainEvent): PublicEventData {
    if (WalletCreated::class.java.isInstance(domainEvent)) {
      val walletCreated = WalletCreated::class.java.cast(domainEvent)
      return WalletCreatedPublicEventData.from(walletCreated)
    }
    // TODO: Use custom exception
    throw RuntimeException("Domain event not supported")
  }
}
