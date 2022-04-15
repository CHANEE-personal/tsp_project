#!/bin/zsh

REPOSITORY=/home/ec2-user/apache-tomcat-
BRANCH=master
SERVER=3.36.127.153

echo 'deploying to api server...'$SERVER
echo 'copying jar file...'
scp ./build/libs/new_tsp_front-0.0.1-SNAPSHOT.jar