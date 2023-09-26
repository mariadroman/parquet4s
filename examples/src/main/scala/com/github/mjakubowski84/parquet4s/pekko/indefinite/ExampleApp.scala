package com.github.mjakubowski84.parquet4s.pekko.indefinite

import org.apache.pekko.Done
import org.apache.pekko.actor.CoordinatedShutdown
import org.apache.pekko.kafka.scaladsl.Consumer.DrainingControl
import org.apache.pekko.stream.scaladsl.Keep

object ExampleApp
    extends App
    with Logger
    with Pekko
    with Kafka
    with RandomDataProducer
    with MessageSource
    with MessageSink {

  startKafka()
  startDataProducer()

  logger.info(s"Starting stream that reads messages from Kafka and writes them to $baseWritePath...")
  val streamControl: DrainingControl[Done] = messageSource
    .toMat(messageSink)(Keep.both)
    .mapMaterializedValue(DrainingControl.apply[Done])
    .run()

  coordinatedShutdown.addTask(CoordinatedShutdown.PhaseServiceStop, "Stopping stream") { () =>
    logger.info("Stopping stream...")
    streamControl.drainAndShutdown()
  }

}
