#!/bin/bash

password="swarch"

server_id="swarch@xhgrid6"

path="fbcallbackJDJS"

start=40


sshpass -p $password ssh $server_id "rm -rf $path && mkdir $path"



sshpass -p $password scp ../server/build/libs/server.jar $server_id:./$path


cd ../

function gradleBuild {
    id=$1
    cd ./client/src/main/resources
    newClientId=$((start+id))
    sed -i "s/^Callback.Client.Endpoints=default -h 192.168.131.*/Callback.Client.Endpoints=default -h 192.168.131.$newClientId/" client.cfg
    cd ../../../../
    gradle build &
    wait

}

IFS=',' read -ra array_client <<< "$1"

for client in "${array_client[@]}"; do

    gradleBuild $client

    client_id="swarch@xhgrid$client"

    sshpass -p $password ssh $client_id "rm -rf $path && mkdir $path"

    sshpass -p $password scp ./client/build/libs/client.jar $client_id:./$path

    echo $client+" ready"

done