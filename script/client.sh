#!/bin/bash

password="swarch"
path="fbcallbackJDJS"

computers="$1"
number="$2"

IFS=',' read -ra array_computers <<< "$computers"

# Nombre del archivo de log
log_file="execution_log.txt"

# Borrar el archivo de log si ya existe
rm -f "$log_file"

for computer in "${array_computers[@]}"; do
    # Ejecutar el comando en paralelo y redirigir la salida estándar y de error al archivo de log
    sshpass -p $password ssh swarch@xhgrid$computer "cd $path && java -jar client.jar $number" 2>&1 | tee -a "$log_file" &
done

# Esperar a que todas las ejecuciones en paralelo finalicen
wait

# Mostrar el contenido del archivo de log en la consola
cat "$log_file"