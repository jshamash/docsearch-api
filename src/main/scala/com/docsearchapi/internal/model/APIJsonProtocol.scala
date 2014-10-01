package com.docsearchapi.internal.model

import spray.httpx.SprayJsonSupport
import spray.json._

object APIJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val documentFormat = jsonFormat2(Document)
  implicit val searchResultFormat = jsonFormat3(SearchResult)
  implicit val highlightFormat = jsonFormat1(Highlight)
  implicit val hitFormat = jsonFormat(Hit, "_source", "_score", "highlight")
  implicit val hitsFormat = jsonFormat1(Hits)
  implicit val esResultFormat = jsonFormat1(ESResult)
}
