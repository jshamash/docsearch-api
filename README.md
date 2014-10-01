docsearch-api
=============

Index &amp; search documents in the cloud.

- Stores documents in MongoDB
- Indexes documents & searches using ElasticSearch

Presently running at [http://docsearch.herokuapp.com](http://docsearch.herokuapp.com).

###JSON Structures
#####Document:

```
{
  "name": "<name of doc>",
  "doc" : "<contents of doc>"
}
```

* The name of the document must be unique.

#####SearchResult:

```
{
  "results" : [
    {"documentName": "<name of doc>",
     "document": "<document>",
     "score": <score>
    },
    ...
  ],
  "count" : <count>
}
```

* `<document>` is the document contents with matched terms highlighted.
* `<score>` is this document's "score", a measure of how well it matches the query.
* `<count>` is the number of results returned.

#####Error:

```
{
  "status": <status code int>,
  "error": "<status code message>",
  "message": "<error message>",
  "info": "<additional info>"
}
```

###API Endpoints
| Method        | Path          | Request Entity  | Response Entity |
| :------------:|:-------------:| :-------------: | :-------------: |
| POST          | /documents    | Document        | The created Document|
| GET           | /documents?q=<query>|   None    | SearchResult (of searching for <query>)|
| GET           | /documents/<name>   |    None   | The Document with name=<name> |
