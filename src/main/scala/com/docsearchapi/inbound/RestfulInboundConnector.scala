package com.docsearchapi.inbound

import akka.actor.ActorRef

import scala.language.postfixOps

class RestfulInboundConnector(db: ActorRef) extends InboundConnector {

  val route = {
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
      }
    }
  }

  def receive = runRoute(route)
}