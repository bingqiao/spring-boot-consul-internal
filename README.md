Spring Boot Consul External
===========================

This project demos the use of [consul](https://www.consul.io/) and [feign](https://github.com/OpenFeign/feign) in a Spring Boot Gradle project.

This is one of the two services for a complete demo.

## Build 

Java 11 is required.

~~~
gradlew clean build unpack
docker build --rm -t consul-internal .
~~~

## Run 

~~~
docker-compose up -d --no-deps --build consul-internal
~~~

## Stop  

~~~
docker-compose stop
~~~

## Publish to local maven repository:

~~~
gradlew clean build publishToMavenLocal
~~~

## Setup Consul

### Docker

~~~
docker pull consul
~~~

~~~
docker run \
    -d \
    -p 8500:8500 \
    -p 8600:8600/udp \
    --name=consul \
    consul agent -dev -ui -node=consul -bootstrap-expect=1 -client=0.0.0.0
~~~

### Installed

~~~
consul agent -dev -node consul
~~~
