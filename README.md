# Parallel Url Reader

The _Parallel Url Reader_ performs a `GET` request to a given URL and parses the retrieved content for additional URLs. The new found URLs will also be visited. This continues, until the specified depth is reached. Every found URL and the number of its occurrences is collected and aggregated to a map, which is finally send as response.

## Retrieving plain JSON

```
curl "http://localhost:8080/urls?rootUrl=https%3A%2F%2Fde.wikipedia.org%2Fwiki%2FWikipedia:Hauptseite&depth=1"
```

## Retrieving a JSON file

```
curl -H "Accept: application/octet-stream" "http://localhost:8080/urls?rootUrl=https%3A%2F%2Fde.wikipedia.org%2Fwiki%2FWikipedia:Hauptseite&depth=1" --output urls.json
```