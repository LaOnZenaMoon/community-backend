# community-backend
* For community project, back-end server
* Will be used with other projects[community-frontend, authentication-backend, spring-cloud-infra]

## community-api
* REST API Documentation 
* http://{{host}}:8880/swagger-ui.html
* (Optional) Load the initial sample data
  * Add the below code in application.yml
```
lozm:
  data:
    enabled: true
    size: 1000
```

## core
* common entities and utils

## skills
* Java 1.8
* Spring Boot
* JPA
  * Spring Data JPA, QueryDSL
* Swagger
* Junit5
* Ehcache
