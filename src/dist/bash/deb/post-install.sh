#!/bin/bash

RAYDIO_USERNAME=raydio
RAYDIO_SERVICE_NAME=raydio

# Create Directories
mkdir -p /opt/raydio/data

# Create User
adduser --system $RAYDIO_USERNAME audio
chown -R $RAYDIO_USERNAME /opt/raydio/

# Register and Start Service
systemctl start $RAYDIO_SERVICE_NAME
systemctl enable $RAYDIO_SERVICE_NAME