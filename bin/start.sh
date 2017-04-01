#!/bin/bash
    PROJECT_PATH=/root/my-code/
	echo "Starting my-code ..."
	nohup java -Xbootclasspath/a:`echo $PROJECT_PATH/lib/*.jar| tr ' ' ':'`: -jar $PROJECT_PATH/lib/my-code*.jar > /root/logs/stdout.log 2>&1 &
	echo "Started my-code ..."