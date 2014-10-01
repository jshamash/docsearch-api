package com.docsearchapi.inbound

import akka.actor.ActorRef
import akka.pattern.ask
import com.docsearchapi.internal.messaging.{GetDocument, IndexDocument}
import com.docsearchapi.internal.model._

import scala.language.postfixOps

class RestfulInboundConnector(db: ActorRef) extends InboundConnector {

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
            complete { db ? IndexDocument(doc) }
          }
        } ~
        parameter('q) { query =>
          get {
            complete { "Search for " + query }
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