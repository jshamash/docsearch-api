package com.docsearchapi.internal.model

import spray.httpx.SprayJsonSupport
import spray.json._

object APIJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val documentFormat = jsonFormat2(Document)
  implicit val searchResultFormat = jsonFormat3(SearchResult)
}
