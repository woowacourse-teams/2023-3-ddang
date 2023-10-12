#!/bin/bash

ABSPATH=$(readlink -f $0) # /home/ubuntu/prod-deploy-script.sh
ABSDIR=$(dirname $ABSPATH) # /home/ubuntu
APPNAME="ddang-0.0.1-SNAPSHOT"
APPDIR=${ABSDIR}/${APPNAME} # /home/ubuntu/ddang-0.0.1-SNAPSHOT
PORT_A=8080
PORT_B=8081
DEFAULT_ACTUATOR_PORT=$DEFAULT_ACTUATOR_PORT

if curl -s "http://localhost:${PORT_A}" > /dev/null
then
    green_port=${PORT_B}
    blue_port=${PORT_A}
else
    green_port=${PORT_A}
    blue_port=${PORT_B}
fi

if curl -s "http://localhost:${green_port}" > /dev/null
then
  echo "그린 서버가 이미 동작 중입니다."
  exit 255
fi

if curl -s "http://localhost:${DEFAULT_ACTUATOR_PORT}" > /dev/null
then
    actuator_port=${ANOTHER_ACTUATOR_PORT}
else
    actuator_port=${DEFAULT_ACTUATOR_PORT}
fi

echo "그린 서버를 실행합니다. port number: ${green_port}"

nohup java -jar ${ABSDIR}/${APPNAME}.jar --server.port=${green_port} --spring.profiles.active=prod --management.server.port=${actuator_port} 1>> prod.log 2>> prod_error.log & # /home/ubuntu/ddang-0.0.1-SNAPSHOT.jar

for retry_count in $(seq 10)
do
    if curl -s "http://localhost:${green_port}" > /dev/null
    then
        echo "Health check success ✅ port number: ${green_port}"
        break
    fi

    if [ $retry_count -eq 10 ]
    then
        echo "Health check failed ❌ port number: ${green_port}"
        exit 1
    fi

    echo "서버가 아직 실행되지 않았습니다... 시도 횟수: ${retry_count}"
    sleep 10
done

echo "set \$service_port ${green_port};" | sudo tee /etc/nginx/conf.d/service-port.inc
echo "set \$actuator_port ${actuator_port};" | sudo tee /etc/nginx/conf.d/actuator-port.inc
sudo systemctl restart nginx

echo "블루 서버를 종료합니다. port number: ${blue_port}"
fuser -s -k ${blue_port}/tcp

if curl -s "http://localhost:${blue_port}" > /dev/null
then
    echo "블루 서버가 아직 종료되지 않았습니다... 시도 횟수: ${blue_port}"
    sleep 10
else
    echo "블루 서버가 종료되었습니다. port number: ${blue_port}"
    break;
fi
