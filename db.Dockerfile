FROM postgres:bullseye

RUN apt-get update
RUN apt-get -y upgrade
RUN apt-get -y install postgis
RUN apt-get -y install postgresql-14-postgis-3-scripts
RUN apt-get -y install osm2pgsql
RUN apt-get -y install sudo
RUN apt-get -y install wget

RUN wget -q -O 'uvic.xml' 'https://www.openstreetmap.org/api/0.6/map?bbox=-123.3257%2C48.4566%2C-123.292%2C48.4695'

ENV POSTGRES_USER "mapuvic"
ENV POSTGRES_PASSWORD "mapuvic"
ENV POSTGRES_DB "mapuvic"
HEALTHCHECK CMD ["pg_isready"]

COPY database/* /docker-entrypoint-initdb.d/

RUN chmod +x docker-entrypoint-initdb.d/*
