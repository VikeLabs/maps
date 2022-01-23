# App

This is the frontend of the mapuvic project.

# Contributing

In order to get fully up and running you need the backend running. The simplest way to do this is via docker

## Running the backend via Docker

Make sure all the following commands are run in the root of the project.

1. build the database docker image.

```shell
docker build -t mapuvic-db -f db.Dockerfile .
```

2. run the database docker image. This needs to be running in order to build the backend itself.

```shell
docker run -p "5432:5432" mapuvic-db
```

3. Build the backend image

```shell
docker build -t mapuvic-backend --network=host -f api.Dockerfile .
```

4. Run the backend.

```shell
docker run -p "8000:8000" --network=host mapuvic-backend
```

## Starting the frontend.

Run the frontend, this installs node, npm, and all the dependencies, then starts a development server, saving files
should reload the app.

```shell
./gradlew app:dev 
```
