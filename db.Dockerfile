FROM postgres:bullseye

RUN apt-get update
RUN apt-get -y upgrade
RUN apt-get -y install postgis
RUN apt-get -y install postgresql-14-postgis-3-scripts
RUN apt-get -y install osm2pgsql
RUN apt-get -y install sudo
RUN apt-get -y install wget

ENV POSTGRES_USER "mapuvic"
ENV POSTGRES_PASSWORD "mapuvic"
ENV POSTGRES_DB "mapuvic"

COPY database/* /docker-entrypoint-initdb.d/

RUN chmod +x docker-entrypoint-initdb.d/*
