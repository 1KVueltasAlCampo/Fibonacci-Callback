#!/bin/bash

password="swarch"
server_id="swarch@xhgrid6"
path="fbcallbackJDJS"
log_file="server_log.txt"

# Detener la ejecución del archivo JAR en el servidor
sshpass -p $password ssh $server_id "cd $path && pkill -f 'java -jar'"

# Ejecutar el archivo JAR en el servidor, redirigir la salida al archivo de log y a la consola
sshpass -p $password ssh $server_id "cd $path && nohup java -jar *.jar 2>&1 | tee $log_file &"

# Mover el archivo de log desde el servidor remoto a la carpeta local
sshpass -p $password scp $server_id:$path/$log_file .