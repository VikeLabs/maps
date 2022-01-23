# api

This is the backend for the app. It provides mapping data, search results and routing for the application.

## Running

`gradlew api:run` will run the server. Note you will need a postgres database running. Runnning the image created by db.Dockerfile should do it.

## Testing

`gradlew api:test` will run all tests for the server. Integration tests may require external services and/or an internet
connection.

## Building

`gradlew api:build` will produce a runnable .jar containing the server.

## Contributing

`gradlew api:ktlintFormat` will format your code, well formatted code is required for pull requests. You can check
formatting with `gradlew api:ktlintCheck`
