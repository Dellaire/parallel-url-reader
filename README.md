# Parallel Url Reader

The _Parallel Url Reader_ performs a `GET` request to a given URL and parses the retrieved conten for additional URLs. These URLs will be called "first level URLs" in scope of this repository. Afterwards, every first level URL is called in parallel by a `GET` request and its response is parsed for URLs too. All URLs (first and second level) are put in a map, which contains the occurence of every URL. The Output is written into a file, which also can be accessed via an HTTP endpoint.

## Usage

### Starting the program with root URL as parameter

The program accepts a parameter, which is interpreted as root URL. The start could look like this:
`java -jar parallel-url-reader-0.0.1-SNAPSHOT.jar https://en.wikipedia.org/wiki/Europe`

The default output file (configurable via `application.properties`) is _urlCounts.txt_.

### Calling the HTTP endpoint for plain text

A plain text response, whish contains an unformated map (more precisely: a `ConcurrentHashMap`) can be obtained by performing an HTTP:

GET http://localhost:8080/url/read?url=https://en.wikipedia.org/wiki/Europe
Accept: text/plain

### Calling the HTTP endpoint for a `Resource`

A formated response, can be obtained by performing an HTTP request:

GET http://localhost:8080/url/read?url=https://en.wikipedia.org/wiki/Europe
Accept: application/json

When callig this endpoint, an output file will be created as well.

https://de.wikipedia.org/wiki/Max_Fechner
http://localhost:8080/urls?rootUrl=https%3A%2F%2Fde.wikipedia.org%2Fwiki%2FMax_Fechner&depth=1