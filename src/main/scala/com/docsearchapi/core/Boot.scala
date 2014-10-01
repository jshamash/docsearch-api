package com.docsearchapi.core

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.docsearchapi.inbound.RestfulInboundConnector
import com.docsearchapi.outbound.{ElasticSearchConnector, MongoDBConnector}
import com.typesafe.config.ConfigFactory
import spray.can.Http

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.Properties

object Boot extends App {
  implicit val system = ActorSystem("Docsearch-API")
  val config = ConfigFactory.load()

  implicit val timeout = Timeout(config.getInt("service.timeout") seconds)

  val dbConnector = system.actorOf(Props[MongoDBConnector], "db-connector")
  val esConnector = system.actorOf(Props[ElasticSearchConnector], "es-connector")
  val inboundConnector = system.actorOf(Props(classOf[RestfulInboundConnector], dbConnector, esConnector), "inbound-connector")

  val iface = config.getString("service.interface")
  val port = Properties.envOrNone("PORT") match {
    case Some(p) => p.toInt
    case None => config.getInt("service.port")
  }

  IO(Http) ? Http.Bind(inboundConnector, interface = iface, port = port)
}
