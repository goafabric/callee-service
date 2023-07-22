#!/bin/bash
mkdir -p /home/project/build/native/nativeCompile && cp /home/project/build/libs/*-SNAPSHOT.jar /home/project/build/native/nativeCompile && cd /home/project/build/native/nativeCompile && jar -xvf *-SNAPSHOT.jar
[[ -f @META-INF/native-image/argfile ]] && echo !!using ARGFILE && ARGFILE=@META-INF/native-image/argfile; native-image -H:Name=myapplication -Ob $ARGFILE -cp .:BOOT-INF/classes:`find BOOT-INF/lib | tr '\n' ':'`
