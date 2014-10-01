package com.docsearchapi.inbound

import com.docsearchapi.core.DocsearchActor
import com.docsearchapi.internal.messaging.{APIException, APIResponseSuccess}
import spray.http.StatusCodes._
import spray.http._
import spray.httpx.marshalling._
import spray.routing.{HttpServiceActor, _}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait InboundConnector extends HttpServiceActor with DocsearchActor {

  implicit def FutureMarshaller(implicit exctx: ExecutionContext) =
    ToResponseMarshaller.of[Future[Any]](ContentTypes.`application/json`) { (value, contentType, ctx) =>
      value onComplete {
        case Success(response: APIResponseSuccess) =>
          ctx.marshalTo(response.marshal)
        case Success(e: APIException) =>
          ctx.marshalTo(e.marshal)
        case Failure(e: APIException) =>
          ctx.marshalTo(e.marshal)
        case Success(e: Exception) =>
          ctx.marshalTo(APIException(e).marshal)
        case Failure(e: Exception) =>
          ctx.marshalTo(APIException(e).marshal)
        case unknown =>
          ctx.marshalTo(HttpResponse(InternalServerError).withEntity(unknown.toString))
      }
    }

  def jsonifyError(response: HttpResponse): HttpResponse = {
    APIException(response.status, response.entity.asString, "No additional info").marshal()
  }

  implicit val rejectionHandler: RejectionHandler = RejectionHandler {
    case rejections => mapHttpResponse(jsonifyError) {
      RejectionHandler.Default(rejections)
    }
  }
}