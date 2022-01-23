#!/bin/bash

set -e
    osm2pgsql \
        --user=$POSTGRES_USER \
        --database=$POSTGRES_DB \
        --output=flex \
        --style="/docker-entrypoint-initdb.d/osm2pgsql.lua" \
        --create "/uvic.xml"
