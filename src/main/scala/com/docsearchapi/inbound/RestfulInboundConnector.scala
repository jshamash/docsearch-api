package com.docsearchapi.inbound

import akka.actor.ActorRef
import akka.pattern.ask
import com.docsearchapi.internal.messaging._
import com.docsearchapi.internal.model._

import scala.language.postfixOps

class RestfulInboundConnector(db: ActorRef, es: ActorRef) extends InboundConnector {

  val route = {
    import APIJsonProtocol._
    sealRoute {
      path("") {
        get {
          complete {
            <html>
              <body>
                <h1>Welcome to the Docsearch API!</h1>
              </body>
            </html>
          }
        }
      } ~
      path("documents") {
        post {
          entity(as[Document]) { doc =>
            complete {
              (db ? IndexDocument(doc)).flatMap{
                case _: APIResponseSuccess => es ? IndexDocument(doc)
                case e: APIException       => throw e
              }
            }
          }
        } ~
        parameter('q) { query =>
          get {
            val strippedQuery = query.split("[^\\w]+").reduce(_ + " " + _)
            complete { es ? Search(strippedQuery) }
          }
        }
      } ~
      path("documents" / Segment) { name =>
        get {
          complete {  db ? GetDocument(name) }
        }
      }
    }
  }

  def receive = runRoute (route)
}