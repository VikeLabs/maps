FROM gradle:7.2.0-jdk11
COPY --chown=gradle:gradle settings.gradle.kts /home/gradle/src/
COPY --chown=gradle:gradle ./api /home/gradle/src/
WORKDIR /home/gradle/src
RUN gradle build
RUN gradle test
EXPOSE 8000:8000
HEALTHCHECK CMD curl --fail http://localhost:8000/ping || exit 1
CMD ["gradle", "run", "--console=plain"]
