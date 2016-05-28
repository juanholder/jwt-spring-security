# Spring Security and JWT Integration Operational Example

This java based maven project, constains a full operation integration of JWT (JSON WEB TOKENS) with spring security.

## Requirements

If you've cloned this repo then you'll need the following software to build it:

1. Java 1.6 or newer
2. Maven 3.1 or newer
3. REST client Tool

## Running the application

Having  MVN and Java installed just execute the following maven goals in order to the the app running:

`mvn clean tomcat:run`

then the app will be binded and listening on:

http://localhost:8080/jwt-base-server/

## Testing the application

This application only exposes RESTFull methods, you should have some REST client tool to invoke those methods. 

**API:** http://localhost:8080/jwt-base-server/api

So we have 2 main group of methods, the security methods and the business methods.

Security methods

| HTTP Method| relative path| headers | Comments |
| :-------------: |:-------------:| -----| -----|
| POST | /security/auth | Accept: application/json, Content-Type: application/json | requires a json body with username and password as properties. A new token is returned in case of succesful login  |
| GET | /security/refresh | Accept: application/json, Content-Type: application/json,Authorization: [current jwt token] | refresh the token returning another valid token|
