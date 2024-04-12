Sample REST CRUD API with Spring Boot, Mysql, JPA and Hibernate
Steps to Setup
1. Clone the application in your local repository 2. Create Mysql database

create database student
3. Change mysql username and password as per your installation

4. Open src/main/resources/application.properties change spring.datasource.username and spring.datasource.password as per your mysql installation 

5. Build and run the app using maven
   mvn package
Alternatively, you can run the app without packaging it using -
```bash
** mvn spring-boot:run **


## The app will start running at http://localhost:8080.

URL for the swagger-ui will be http://localhost:8080/api/swagger-ui/index.html?configUrl=/api/v3/api-docs/swagger-config

Explore Rest APIs

The app defines following CRUD APIs.

POST /api/v1/authenticate
##Initial endpoint for authentication PAYLOAD { "username": "priyanshu@123", "password": "spring@123" }

POST /api/v1/add-admin
##To add user with ADMIN Role which is accessible to SUPER_ADMIN_ROLE only

GET /api/v1/users
##To fetch an alll user(s) which is accessible by any ROLE

POST /api/v1/users
##To add user with user role which is accessible for ADMIN_ROLE only

GET /api/v1/users/{userId}
##To fetch an user with an Id which is accessible by any ROLE

PUT /api/v1/users/{userId}
## To update an user which is accessible for any ADMIN_ROLE only

DELETE /api/v1/users/{userId}
## To delete an user which is accessible for SUPER_ADMIN only 