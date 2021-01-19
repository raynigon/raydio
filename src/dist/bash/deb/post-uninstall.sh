#!/bin/bash

RAYDIO_DATA_DIRECTORY=/opt/raydio/data/
SQLITE_DATABASE_FILE=/opt/raydio/data/raydio.sqlite

if [ -f "$SQLITE_DATABASE_FILE" ]; then
  rm "$SQLITE_DATABASE_FILE"
fi

if [ -z "$(ls -A $RAYDIO_DATA_DIRECTORY)" ]; then
  rm -r "$RAYDIO_DATA_DIRECTORY"
fi
