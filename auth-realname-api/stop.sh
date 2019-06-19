#!/bin/bash

server_name=auth-realname-api.jar

kill `ps -ef|grep java|grep $server_name|awk '{print $2}'` > /dev/null 2>&1

if [ $? -ne 0 ]; then
	echo "kill $server_name failed!"
else
	echo "kill $server_name success!"
fi

exit 0
