[![Build and Test](https://github.com/MarcusDunn/maps/actions/workflows/docker-test.yml/badge.svg)](https://github.com/MarcusDunn/maps/actions/workflows/docker-test.yml)

# maps

This is a [VikeLabs](https://www.vikelabs.ca/) project meant for navigating around UVic. It's structured as a
multi-module gradle project with an api server and a web app (in `api` and `app` respectivly). You can find out more
about the details of those modules in their respective `README.md`'s.

## Running

You can run the backend with `gradlew api:run` and the frontend with `gradlew app:run`. This should install dependencies
you do not already have, so it may take a while first time! Sadly, due to issues with halting (darn Turing), we cannot
run both in a single gradle command. `gradlew run` will run the backend `api` and wait for it to end (which it doesn't)
then run the frontend `app`.

## Testing

You can test everything at once with `gradlew test`. Or individual projects with `gradlew app:test`
or `gradlew api:test`

## Building / Deploying

You can build both projects (producing an optimized app and a jar file) with `gradlew build`. Similar to testing and
running, you can build the individual projects with `gradlew app:build` and `gradlew api:build` 
