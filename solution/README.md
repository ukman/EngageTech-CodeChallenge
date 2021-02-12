README
====
# EngageTech. Code Challenge. Backend
Code challenge for EngageTech. Spring Boot Backend.
## Database configuration
The app is designed to work with PostgreSQL (9.1.6+). 
In order to configure DB open `./src/main/resources/application.yml` and configure DB
```yml
  # Data source configuration
  datasource:
    url: jdbc:postgresql://localhost:5432/engagecc
    driver-class-name: org.postgresql.Driver
    username: engagecc
    password: password
```
DB will be created after the very first start of the app. On production reconfigure 
`./src/main/resources/application.yml` after the very first start.
```yml
  jpa:
    generate-ddl: false
    hibernate:
      ddl-auto: none
```

## Start
In order to start the backend open terminal and type
```bash
./gradlew bootRun
```

## Using Swagger
Open http://localhost:8000/swagger-ui/index.html for Swagger UI. You can investigate the RESt API, 
but currently it's available for tests in Swagger UI page because Swagger does not provide 
Basic Authentication header for the API. After redesigning to OpenID this will work. 

## Using PostMan
Open PostMan and import http://localhost:8000/v3/api-docs . 
Use login = `admin` , password = `password` for basic authorization in PostMan. 

## Using Chrome
CORS problem in Angular are currently not configured so disable security as described here
https://alfilatov.com/posts/run-chrome-without-cors/ for your OS

IMPORTANT
====
To avoid unconcious bias, we aim to have your submission reviewed anonymously by one of our engineering team. Please try and avoid adding personal details to this document such as your name, or using pronouns that might indicate your gender.
