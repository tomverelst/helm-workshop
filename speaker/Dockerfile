FROM node:8.16.0-alpine as uideps
WORKDIR /usr/src/app

COPY ui/package.json package.json
COPY ui/package-lock.json package-lock.json
RUN npm install

FROM maven:3.6.0-jdk-11-slim as serverdeps
WORKDIR /usr/src/app
COPY pom.xml /usr/src/app/pom.xml
RUN mvn dependency:go-offline

FROM node:8.16.0-alpine as uidist
WORKDIR /usr/src/app

COPY ui/ .
COPY --from=uideps /usr/src/app/node_modules/ node_modules/
RUN npm run build

FROM maven:3.6.0-jdk-11-slim as serverjar
WORKDIR /usr/src/app
COPY --from=serverdeps /root/.m2 /root/.m2
COPY pom.xml pom.xml
COPY src/ src/

COPY --from=uidist /usr/src/app/dist/ui src/main/resources/static/
RUN mvn clean package

FROM openjdk:11.0.4-jre
COPY --from=serverjar /usr/src/app/target/speaker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]





