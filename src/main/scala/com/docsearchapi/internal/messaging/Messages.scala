package com.docsearchapi.internal.messaging

import spray.http.HttpResponse


trait Message {}

trait DBRequest extends Message

trait DBResponse extends Message

trait APIResponse extends Message {
  def marshal(): HttpResponse
}

trait APIResponseSuccess extends APIResponse
