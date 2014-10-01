package com.docsearchapi.outbound

import com.docsearchapi.internal.messaging._
import com.mongodb.{BasicDBObject, MongoURI}
import spray.json._
import com.docsearchapi.internal.model._
import APIJsonProtocol._

class MongoDBConnector extends OutboundConnector {

  val uri = config.getString("mongodb.uri")
  val collectionName = config.getString("mongodb.collection")

  val mongoUri = new MongoURI(uri)
  val db = mongoUri.connectDB()
  val collection = db.getCollection(collectionName)

  def receive = {
    case IndexDocument(doc) =>
      try {
        val obj = new BasicDBObject("name", doc.name).append("doc", doc.doc)
        collection.insert(obj)
        sender ! DocCreated(doc)
      } catch {
        case e: Exception => sender ! APIException(e)
      }

    case GetDocument(name) =>
      try {
        val found = collection.findOne(new BasicDBObject("name", name))
        val doc = found.toString.parseJson.convertTo[Document]
        sender ! DocumentInfo(doc)
      } catch {
        case e: Exception => sender ! APIException(e)
      }

    case unknown => log.warning("Got unknown: " + unknown)
  }
}
