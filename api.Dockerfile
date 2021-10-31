FROM gradle:7.2.0-jdk11 AS build
COPY --chown=gradle:gradle ./settings.gradle /home/gradle/src/
COPY --chown=gradle:gradle ./api /home/gradle/src/
WORKDIR /home/gradle/src
RUN gradle build
RUN gradle test
EXPOSE 8000:8000
CMD ["gradle", "run"]