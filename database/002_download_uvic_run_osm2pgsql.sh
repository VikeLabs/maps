#!/bin/bash

set -e

osm2pgsql \
    --user=$POSTGRES_USER \
    --database=$POSTGRES_DB \
    --latlong \
    --output=flex \
    --style=/docker-entrypoint-initdb.d/osm2pgsql.lua \
    -c uvic.xml
