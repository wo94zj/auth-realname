#!/bin/bash

target_path=$(cd `dirname $0`; pwd)
cd $target_path

java -XX:+UseG1GC -Xmx1024m -XX:MaxGCPauseMillis=200 -XX:GCPauseIntervalMillis=300 -jar auth-realname-api.jar --spring.profiles.active=prod & > /dev/null

exit 0
