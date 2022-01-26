# api

This is the backend for the app. It provides mapping data, search results and routing for the application.

## Contributing

In order to build the backend one needs the database running.

### Running the database via Docker

A already built image is hosted on dockerhub.

```shell
docker run -p "5432:5432" quality11/mapuvic-database:0.0.3
```

Should run it. Run `docker ps` and there should be a single container running.

### Running the backend

Running the backend itself is done through gradle.

```
./gradlew api:run
```
Will start a server. When you see `server started at localhost:8000`, it is ready for connections.

### Developing on the frontend

The frontend is best devloped with
```
./gradlew app:dev
```
This should enable hot reloading and other nice features.
