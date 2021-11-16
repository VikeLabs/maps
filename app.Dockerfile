FROM gradle:7.2.0-jdk11
COPY ["api/src/test/resources/ca/vikelabs/maps/OpenApiTest.check approved.approved", "/home/gradle/src/api/src/test/resources/ca/vikelabs/maps/OpenApiTest.check approved.approved"]
COPY --chown=gradle:gradle settings.gradle.kts /home/gradle/src
COPY --chown=gradle:gradle ./app /home/gradle/src/app
WORKDIR /home/gradle/src
RUN gradle app:build
RUN gradle app:test
EXPOSE 5000:5000
HEALTHCHECK CMD curl --fail http://localhost:5000 || exit 1
CMD ["gradle", "app:run", "--console=plain"]
