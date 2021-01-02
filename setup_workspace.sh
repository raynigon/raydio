#!/bin/bash

# Setup Static Content as Symbolic Link in Resources Folder
cd src/main/resources/
ln -s ../../../frontend/dist/raydio/ static
cd ../../../