#!/bin/bash

ABSPATH=$(readlink -f $0) # /home/ubuntu/dev-deploy-script.sh
ABSDIR=$(dirname $ABSPATH) # /home/ubuntu
APPNAME="ddang-0.0.1-SNAPSHOT"
APPDIR=${ABSDIR}/${APPNAME} # /home/ubuntu/ddang-0.0.1-SNAPSHOT

echo "구동중인 애플리케이션을 확인합니다."

CURRENT_PID=$(pgrep -f ${APPNAME}.jar) # ddang-0.0.1-SNAPSHOT.jar

if [ ! -z ${CURRENT_PID} ]; then
        echo "기존 애플리케이션이 실행중이므로 종료합니다."
        kill -15 ${CURRENT_PID}
        sleep 5
fi

echo "애플리케이션을 실행합니다."

nohup java -jar ${ABSDIR}/${APPNAME}.jar --spring.profiles.active=dev 1>> dev.log 2>> dev_error.log & # /home/ubuntu/ddang-0.0.1-SNAPSHOT.jar
