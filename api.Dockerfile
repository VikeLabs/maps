FROM gradle:7.3.3-jdk17
COPY --chown=gradle:gradle settings.gradle.kts /home/gradle/src/
COPY --chown=gradle:gradle ./api /home/gradle/src/api
COPY --chown=gradle:gradle ./database/ /home/gradle/src/database
COPY --chown=gradle:gradle ./db.Dockerfile /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle api:build
EXPOSE 8000:8000
HEALTHCHECK CMD curl --fail http://localhost:8000/ping || exit 1
CMD ["gradle", "api:run", "--console=plain"]
