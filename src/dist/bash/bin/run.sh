#!/bin/bash
PATH_TO_WORKSPACE=/opt/raydio/
PATH_TO_CONFIG=/etc/raydio/main.cfg
PATH_TO_JAR=/opt/raydio/bin/raydio.jar

# Load Configuration
config_map=$(cat $PATH_TO_CONFIG)
IFS=$'\n'
for entry in $config_map; do
    if [[ "$entry" =~ ^\s*#.* ]] || [[ ${entry} != *"="* ]]; then
      continue
    fi
    IFS=$' '
    set -- `echo $entry | tr '=' ' '`
    IFS=$'\n'
    key=$1
    val=$2

    export "$key=$val"
done
IFS=$' '

# Start Application
cd "$PATH_TO_WORKSPACE"
java -jar $PATH_TO_JAR >> log.txt