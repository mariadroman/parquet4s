package com.github.mjakubowski84.parquet4s.pekko.indefinite

import org.apache.pekko.actor.{ActorSystem, CoordinatedShutdown}

import scala.concurrent.ExecutionContext

trait Pekko {

  this: Logger =>

  implicit lazy val system: ActorSystem           = ActorSystem()
  implicit def executionContext: ExecutionContext = system.dispatcher
  val coordinatedShutdown: CoordinatedShutdown    = CoordinatedShutdown(system)

}
