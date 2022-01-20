#!/bin/bash

set -e

wget -O - 'https://www.openstreetmap.org/api/0.6/map?bbox=-123.3257%2C48.4566%2C-123.292%2C48.4695' |
    osm2pgsql \
        --user=$POSTGRES_USER \
        --database=$POSTGRES_DB \
        --input-reader=xml \
        --hstore \
        -c -
