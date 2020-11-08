## **_portfolio-tracking-api_**

The API is written in java 1.8 using Spring boot framework and the database used is H2(in-memory database) . 

H2 database can easily be integrated with the spring and therefore no need to manage external database servers and configs

Command to run jar :-

java -jar -Dserver.port=8080 portfolio-tracking-api.jar

Base Documentation can be found at :- http://localhost:8000/swagger-ui.html

Health of the API can be checked at :- http://localhost:8000/actuator/health

Database can be found at :- http://localhost:8000/h2-console

Base URL :-  http://localhost:8000/api/portfolio-tracking
