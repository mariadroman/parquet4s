package com.github.mjakubowski84.parquet4s.pekko.indefinite

import org.apache.pekko.kafka.scaladsl.Consumer
import org.apache.pekko.kafka.{ConsumerMessage, ConsumerSettings, Subscriptions}
import org.apache.pekko.stream.scaladsl.Source
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.duration.Duration

object MessageSource {

  type Message = ConsumerMessage.CommittableMessage[String, String]

}

trait MessageSource {

  this: Pekko & Kafka =>

  import MessageSource.*

  private val consumerSettings = ConsumerSettings(system, new StringDeserializer(), new StringDeserializer())
    .withBootstrapServers(kafkaAddress)
    .withGroupId(groupId)
    .withStopTimeout(Duration.Zero)
  private val subscription = Subscriptions.topics(topic)

  lazy val messageSource: Source[Message, Consumer.Control] = Consumer.committableSource(consumerSettings, subscription)

}
