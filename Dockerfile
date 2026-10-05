FROM eclipse-temurin:21-jdk-alpine
WORKDIR e-commerece/app
COPY target/E-Commerce-0.0.1-SNAPSHOT.jar /e-commerece/app/E-Commerce.jar
CMD ["java","-jar","E-Commerce.jar"]