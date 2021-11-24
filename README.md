[![Build and Test](https://github.com/MarcusDunn/maps/actions/workflows/docker-test.yml/badge.svg)](https://github.com/MarcusDunn/maps/actions/workflows/docker-test.yml)

# maps

This is a [VikeLabs](https://www.vikelabs.ca/) project meant for navigating around UVic. It's structured as a
multi-module gradle project with an api server and a web app (in `api` and `app` respectively). You can find out more
about the details of those modules in their respective `README.md`'s.

## Running

You can run the backend with `gradlew api:run` and the frontend with `gradlew app:dev` (`run` also works but dev gives
nice hot loading features). This should install dependencies you do not already have, so it may take a while first time!

## Testing

You can test everything at once with `gradlew test`. Or individual projects with `gradlew app:test`
or `gradlew api:test`. I recommend also running `gradlew check` before every push as it runs potentially more tests as
well as linters and such but may be slower as a result.

## Building

You can build both projects (producing an optimized app and a jar file) with `gradlew build`. Similar to testing and
running, you can build the individual projects with `gradlew app:build` and `gradlew api:build`

## Deploying

Two Dockerfiles (`api.Dockerfile` and `app.Dockerfile`) are tested every push. They run optimized builds and as a result
are the best way to deploy the app in a long-running environment.

### Some notes on gradle

You can of course run everything here *without* gradle, you may want to directly run `npm` commands for example. I would
recommend against it as there is a curated dependency graph for tasks with gradle whereas for npm you are on your own.
If any mentioned gradle commands fail for any reason beyond broken code, that is a bug and should be filed as such.
