FROM openjdk:17-alpine3.14
RUN wget http://s3.localhost.localstack.cloud:4566/release-bucket/second-app-1.0-SNAPSHOT.jar
WORKDIR /
CMD ["java", "-jar", "/second-app-1.0-SNAPSHOT.jar"]