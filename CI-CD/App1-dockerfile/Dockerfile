FROM openjdk:17-alpine3.14
RUN wget http://s3.localhost.localstack.cloud:4566/release-bucket/first-app-1.0-SNAPSHOT.jar
WORKDIR /
CMD ["java", "-jar", "/first-app-1.0-SNAPSHOT.jar"]