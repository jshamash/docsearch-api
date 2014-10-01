package com.docsearchapi.core

import com.typesafe.config.ConfigFactory
import akka.util.Timeout
import akka.actor.Actor
import scala.language.postfixOps
import scala.concurrent.duration._
import akka.actor.ActorLogging

trait DocsearchActor extends Actor with ActorLogging {

  val config = ConfigFactory.load()
  implicit val timeout = Timeout(config.getInt("service.timeout") seconds)
  implicit val executionContext = context.dispatcher
}