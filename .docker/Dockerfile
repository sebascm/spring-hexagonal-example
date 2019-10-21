FROM circleci/openjdk:8-jdk-node

ADD application/target/application-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT exec java -jar app.jar