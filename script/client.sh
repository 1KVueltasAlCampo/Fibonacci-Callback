#!/bin/bash

password="swarch"
server_id="swarch@xhgrid6"
path="fbcallbackJDJS"

computers="$1"
number="$2"

IFS=',' read -ra array_computers <<< "$computers"

for computer in "${array_computers[@]}"; do
    sshpass -p $password ssh $server_id "sshpass -p $password ssh swarch@xhgrid$computer 'cd $path && echo $number | java -jar *.jar' &"
    echo "xhgrid$computer"
done
