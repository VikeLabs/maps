FROM gradle:7.2.0-jdk11 AS build
COPY --chown=gradle:gradle settings.gradle /home/gradle/src
COPY --chown=gradle:gradle ./api /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon
RUN gradle test --no-daemon
CMD ["gradle", "run", "--no-daemon"]