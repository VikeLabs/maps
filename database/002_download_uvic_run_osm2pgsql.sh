#!/bin/bash

set -e

wget -O - 'https://www.openstreetmap.org/api/0.6/map?bbox=-123.3229%2C48.4563%2C-123.2999%2C48.4709' | osm2pgsql --user=$POSTGRES_USER --database=$POSTGRES_DB --input-reader=xml -c -
