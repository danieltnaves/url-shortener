# URL Shortener

Most of us are familiar with seeing URLs like bit.ly or t.co on our Twitter or Facebook feeds. These are examples of shortened URLs, which are a short alias or pointer to a longer page link. For example, I can send you the shortened URL http://bit.ly/SaaYw5 that will forward you to a very long Google URL with search results on how to iron a shirt.


## URL Shortener Architecture

<p align="center">
  <img src="https://user-images.githubusercontent.com/1865566/51440538-763bdd00-1caf-11e9-9d09-0b7a11e9e7e9.png">
</p>

**Key Generator** - Is a Spring Boot App responsible for generating 7 letters unique keys. The key generation process is asynchronous and scheduled by Spring Scheduler. For each scheduled execution, a new chunk of keys will be saved in the database. The chunk size can be changed via properties the default value is (**chunk.size = 1000**) Another important note is that the scheduler only generates more keys if the numbers of available keys are less than **chunk.size * 10**.

**REST API** - Is a Spring Boot App responsible for exposing REST endpoint. The API has the following operations:

* **POST** - */shortener* Creates a new URL
* **GET** - */shortener/{key}* - Redirect a short URL to original URL
* **GET** - */shortener/{key}/statistics* - Get statistics for a shortened URL

This API is documented using **Swagger** and can be accessed [here](http://localhost:8080/swagger-ui.html "http://localhost:8080/swagger-ui.html").

## How to execute the application? 

To deploy and execute this application in your machine you need the following requirements installed:

* Maven
* Docker
* Docker Compose

To build and deploy the application execute the following command:

~~~~
sh build-and-deploy.sh
~~~~

This command builds the Key Generator and API using Maven after that uses Docker Compose to build required images and start the containers. If everything works fine you should be able to access Swagger UI using URL: [link](http://localhost:8080/swagger-ui.html "http://localhost:8080/swagger-ui.html")

To destroy all infrastructure, build and deploy again you can use the following command: 

~~~~
sh destroy-and-deploy.sh
~~~~

## How to use the application? 

The easiest way to use the application is using [Swagger UI](http://localhost:8080/swagger-ui.html "http://localhost:8080/swagger-ui.html")
 to do HTTP request, but you can use any HTTP client (Postman, cURL, etc).
 
 ### Sample HTTP requests using cURL
 
**Creates a new shortened URL:**

Request:
~~~~
curl -X POST "http://localhost:8080/shortener" -H "accept: application/json" -H "Content-Type: application/js
~~~~

Response:
~~~~
HTTP Status: 201
{"shortenedURL":"http://localhost:8080/shortener/YTZjZDg"}
~~~~

**Redirect URL:**

Request:
~~~~
Just access the shortened URL in your browser: http://localhost:8080/shortener/YTZjZDg and you will be redirected.
~~~~

**Get statistics from a given URL:**

Request:
~~~~
curl -X GET "http://localhost:8080/shortener/YTZjZDg/statistics" -H "accept: */*"
~~~~

Response:
~~~~
HTTP Status: 200
{
  "shortUrl": "http://localhost:8080/shortener/YTZjZDg",
  "originalUrl": "https://github.com/danieltnaves",
  "creation": "2019-01-20T14:06:29.792Z",
  "hits": 1,
  "lastHit": "2019-01-20T14:07:32.531Z"
}
~~~~

**PS: Note that browsers caches MOVED_PERMANENTLY(301, "Moved Permanently") redirects, to simulate hits and lastHist you will need to access URL using a different browser or clear the cache.** 



