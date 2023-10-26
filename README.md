# test-reactive-jaxrs

This small Quarkus application illustrates a minor problem with media type versioning. If somebody tries to access the REST resource with a unexpected content-type, two different exceptions are thrown depending on the number of methods implemented inside the REST resource. If the resource only has a single method, the thrown exception is a jakarta.ws.rs.NotSupportedException but when the resource has more than one methods an jakarta.ws.rs.WebApplicationException is thrown.

It is not really the biggest problem in the world, but my exception mappers got confused and I ended up spending quite some time debugging my application.

I don't believe (not 100% sure) I have seen this problem before I started using the quarkus-resteasy-reactive-jsonb extension (which is recommended by quarkus). I have used quarkus-resteasy-jsonb previously.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

## Testing the behaviour

You can run a curl with a unexpected content-type (application/vnd.mycompany.baduser.v1+json) by running this script:

```shell script
./test-run.sh
```
The output from the script is:

```
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /hello HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.81.0
> Content-Type: application/vnd.mycompany.baduser.v1+json
> Accept: application/vnd.mycompany.greeting.v1+json
> Content-Length: 44
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 415 Unsupported Media Type
< content-length: 134
< Content-Type: application/vnd.mycompany.error.v1+json
< 
* Connection #0 to host localhost left intact
{"cause":"Unexpected exception","exception-class":"jakarta.ws.rs.WebApplicationException","message":"HTTP 415 Unsupported Media Type"}
```
The json is generated by my exception mapper https://github.com/fin-steenbjerg-jyskebank-dk/test-reactive-jaxrs/blob/main/src/main/java/dk/jyskebank/test/RuntimeExceptionMapper.java

As can be seen, the exception thrown is an instance of jakarta.ws.rs.WebApplicationException.

If the method  dk.jyskebank.test.GreetingResource.helloV2 is removed, then the outcome of the above script is:

```
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /hello HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.81.0
> Content-Type: application/vnd.mycompany.baduser.v1+json
> Accept: application/vnd.mycompany.greeting.v1+json
> Content-Length: 44
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 415 Unsupported Media Type
< content-length: 167
< Content-Type: application/vnd.mycompany.error.v1+json
< 
* Connection #0 to host localhost left intact
{"cause":"Unexpected exception","exception-class":"jakarta.ws.rs.NotSupportedException","message":"The content-type header value did not match the value in @Consumes"}
```

If I remove my exception mapper then both cases result in the same http status code and message: HTTP/1.1 415 Unsupported Media Type. But it would be nice if the two cases would result in the same exception being thrown. I believe that jakarta.ws.rs.NotSupportedException is the best of the two exceptions.