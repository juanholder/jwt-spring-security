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

| HTTP Method| Relative path| Headers | Comments |
| :-------------: |:-------------:| -----| -----|
| POST | /security/auth | Accept: application/json, Content-Type: application/json | Requires a json body with username and password as properties. A new token is returned in case of succesful login  |
| GET | /security/refresh | Accept: application/json, Content-Type: application/json,Authorization: [current jwt token] | Refresh the token returning another valid token|

Business methods

| HTTP Method| Relative path| Headers | Comments |
| :-------------: |:-------------:| -----| -----|
| GET | /business/myBusinessMethod |Accept: application/json, Content-Type: application/json,Authorization: [current jwt token] | Invoke user role granted method |
| GET | /admin/myAdminMethod | Accept: application/json, Content-Type: application/json,Authorization: [current jwt token] | Invoke an admin role granted method|

**User and Roles**

The users and roles are declared in the applicationContext.xml spring descriptor.

The default configuration is the following:

~~~
	<!-- the authentication manager. -->
	<authentication-manager alias="authenticationManager">
		<authentication-provider>
			<!-- Dummy users/pass/roles -->
			<user-service>
				<user name="bill" password="bill" authorities="ROLE_USER" />
				<user name="admin" password="admin" authorities="ROLE_ADMIN,ROLE_USER" />
				<user name="sadmin" password="sadmin" authorities="ROLE_SADMIN,ROLE_ADMIN,ROLE_USER" />
			</user-service>
		</authentication-provider>
	</authentication-manager>
~~~

**Misc.**

User & Pass json body example:

~~~
{
    "username":"admin",
    "password": "admin"
}
~~~

New toke response example:
~~~
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZGllbmNlIjoid2ViIiwiY3JlYXRlZCI6MTQ2NDQxODIwNzE2NiwiZXhwIjoxNDY1MDIzMDA3fQ.Ltp56b8zHn9TBGwUhPkVkxIMMthpmV8163qK_7x0g16IUkoP86OjtRDZKalma7cwkGA_j-SEaJ7On2bRSncoGQ"
}
~~~
