package com.docsearchapi.internal.messaging

import com.docsearchapi.internal.model._
import spray.http._
import APIJsonProtocol._
import spray.json._


trait Message {}

trait DBRequest extends Message

trait DBResponse extends Message

trait APIResponse extends Message {
  def marshal(): HttpResponse
}

trait APIResponseSuccess extends APIResponse

case class IndexDocument(doc: Document) extends DBRequest
case class Search(searchTerms: String) extends DBRequest
case class GetDocument(name: String) extends DBRequest

case class DocCreated(doc: Document) extends APIResponseSuccess {
  def marshal() = HttpResponse(StatusCodes.Created, HttpEntity(ContentTypes.`application/json`, doc.toJson.toString()))
}
case class SearchResponse(results: List[SearchResult]) extends APIResponseSuccess {
  def marshal() = HttpResponse(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, results.toJson.toString()))
}
case class DocumentInfo(doc: Document) extends APIResponseSuccess {
  def marshal() = HttpResponse(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, doc.toJson.toString()))
}