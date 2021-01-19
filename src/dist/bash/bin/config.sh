#!/bin/bash

# Read config
config_map=$(cat /etc/raydio/main.cfg)
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
    # Write this to stdout to execute it later with 'eval'
    echo "export '$key=$val'"
done