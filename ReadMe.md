Sample REST CRUD API with Spring Boot, Mysql, JPA and Hibernate
Steps to Setup
1. Clone the application in your local repository 2. Create Mysql database

create database student
3. Change mysql username and password as per your installation

open src/main/resources/application.properties
change spring.datasource.username and spring.datasource.password as per your mysql installation 4. Build and run the app using maven
mvn package
Alternatively, you can run the app without packaging it using -
```bash
mvn spring-boot:run
The app will start running at http://localhost:8080.
URL for the swagger-ui will be http://localhost:8080/api/swagger-ui/index.html?configUrl=/api/v3/api-docs/swagger-config
Explore Rest APIs
The app defines following CRUD APIs.

GET /api/v1/users

POST /api/v1/users

GET /api/v1/users/{userId}

PUT /api/v1/users/{userId}

DELETE /api/v1/users/{userId}