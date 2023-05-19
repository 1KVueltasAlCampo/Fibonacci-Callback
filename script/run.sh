#!/bin/bash

numbers="$1"
x="$2"

# Ejecutar deploy.sh con la lista de números como argumento
./deploy.sh "$numbers"

# Ejecutar server.sh
./server.sh &

# Esperar unos segundos para que el servidor se inicie correctamente
sleep 3

# Ejecutar client.sh con la lista de números y "x" como argumentos
./client.sh "$numbers" "$x"
