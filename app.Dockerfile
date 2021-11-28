FROM gradle:7.2.0-jdk11
COPY api/src/test/resources/openapi.json /home/gradle/src/api/src/test/resources/openapi.json
COPY --chown=gradle:gradle settings.gradle.kts /home/gradle/src
COPY --chown=gradle:gradle ./app /home/gradle/src/app
WORKDIR /home/gradle/src
RUN gradle app:build
EXPOSE 3000:3000
HEALTHCHECK CMD curl --fail http://localhost:3000 || exit 1
CMD ["gradle", "app:run", "--console=plain"]
