FROM eclipse-temurin:17-jre-alpine

ENV PROFILE PROD

COPY build/install/room-of-horrors /room-of-horrors

WORKDIR /room-of-horrors

EXPOSE 8080

ENTRYPOINT ["bin/room-of-horrors"]