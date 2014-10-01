package com.docsearchapi.internal.model

case class Highlight(doc: List[String])
case class Hit(doc: Document, score: Double, highlight: Highlight)
case class Hits(hits: List[Hit])
case class ESResult(hits: Hits) {
  def toSearchResults = hits.hits.map {hit =>
    SearchResult(hit.doc.name, hit.highlight.doc.reduce(_ + "\n" + _), hit.score)
  }
}