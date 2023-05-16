#!/bin/bash
password="swarch"

server_id="swarch@xhgrid6"

path="fbcallbackJDJS"

start=40


sshpass -p $password ssh -o StrictHostKeyChecking=no $server_id "mkdir $path"



sshpass -p $password scp -o StrictHostKeyChecking=no ../server/build/libs/server.jar $server_id:./$path





cd ../

function gradleBuild {

    id=$1

    gradle build &

    wait

}



IFS=',' read -ra array_client <<< "$1"

for client in "${array_client[@]}"; do

    gradleBuild $client

    client_id="swarch@xhgrid$client"

    sshpass -p $PASSWORD ssh -o StrictHostKeyChecking=no $client_id "mkdir $path"

    sshpass -p $PASSWORD scp -o StrictHostKeyChecking=no ./client/build/libs/client.jar $client_id:./$path

done