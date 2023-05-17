#!/bin/bash

password="swarch"
server_id="swarch@xhgrid6"
path="fbcallbackJDJS"
log_file="server_log.txt"

# Ingresar al servidor y matar los procesos que utilizan el puerto 1234
sshpass -p $password ssh $server_id "cd $path && pkill -f 'java -jar'"

# Ejecutar el archivo JAR en el servidor
sshpass -p $password ssh $server_id "cd $path && nohup java -jar *.jar"

# Esperar un tiempo para que el servidor se ejecute (ajusta el valor según sea necesario)
sleep 10

# Obtener información del servidor y guardarla en el archivo de registro
sshpass -p $password ssh $server_id "ps aux | grep 'java -jar' >> $path/$log_file"

# Descargar el archivo de registro al directorio local
sshpass -p $password scp $server_id:$path/$log_file .

# Eliminar el archivo de registro del servidor (opcional, si no deseas mantenerlo)
sshpass -p $password ssh $server_id "rm $path/$log_file"
