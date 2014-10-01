package com.docsearchapi.internal.messaging

import spray.http.MediaTypes._
import spray.http.{HttpEntity, HttpResponse, StatusCode, StatusCodes}
import spray.json.DefaultJsonProtocol._
import spray.json._

case class APIException(
                         code: StatusCode,
                         msg: String,
                         info: String) extends RuntimeException(msg) with APIResponse {
  // Representation of the error returned to client
  def marshal() = {
    val obj: Map[String, JsValue] = Map(
      "status" -> JsNumber(code.intValue),
      "error" -> JsString(code.reason),
      "message" -> JsString(msg),
      "info" -> JsString(info)
    )
    val json = obj.toJson
    HttpResponse(code, HttpEntity(`application/json`, json.toString))
  }
}

object APIException {
  def apply(e: Exception): APIException = new APIException(
    StatusCodes.InternalServerError,
    "Uncaught exception",
    e.toString)
}