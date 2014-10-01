package com.docsearchapi.outbound
import com.docsearchapi.internal.messaging._
import com.docsearchapi.internal.model.{ESResult, APIJsonProtocol}
import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig.Builder
import io.searchbox.core.{SearchResult, Index}
import spray.http.StatusCodes
import spray.json._
import APIJsonProtocol._

class ElasticSearchConnector extends OutboundConnector {

  val uri = config.getString("elasticsearch.uri")
  val indexName = config.getString("elasticsearch.index")

  val factory = new JestClientFactory()
  factory.setHttpClientConfig(new Builder(uri).multiThreaded(true).build())
  val client = factory.getObject

  def receive = {
    case IndexDocument(document) =>
      val index = new Index.Builder(document).index(indexName).`type`("document").build()
      val res = client.execute(index)
      if (res.isSucceeded) sender ! DocCreated(document)
      else sender ! APIException(StatusCodes.BadGateway, "ElasticSearch error", res.getErrorMessage)

    case Search(query) =>
      val search = new io.searchbox.core.Search.Builder(json(query))
                          .addIndex(indexName)
                          .addType("document")
                          .build()
      val res = client.execute(search).getJsonString.parseJson.convertTo[ESResult]
      sender ! SearchResponse(res.toSearchResults)

    case unknown => log.warning("Got unknown: " + unknown)
  }

  def json(query: String) =
    s"""{
          "query": {
            "bool": {
              "should": [
                {"match": {
                  "name": {
                    "query": "$query",
                    "boost": 2
                  }
                }},
                {"match": {
                  "doc" : "$query"
                }}
              ],
              "minimum_should_match" : 1
            }
          },
          "highlight": {
            "fields": {
              "doc" : {}
            }
          }
        }
    """
}
