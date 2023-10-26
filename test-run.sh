#!/bin/bash

url_local=http://localhost:8080/hello

curl -v -d @test-data.json -H "Content-Type: application/vnd.mycompany.baduser.v1+json" -H "Accept: application/vnd.mycompany.greeting.v1+json" $url_local