#!/bin/bash
#gradle clean bootJar && \
docker run --rm --name native-image-builder --mount type=bind,source=$(pwd)/build,target=/build --entrypoint /bin/bash ghcr.io/graalvm/native-image-community:17.0.8 \
-c 'mkdir -p /build/native/nativeCompile && cp /build/libs/*-SNAPSHOT.jar /build/native/nativeCompile && cd /build/native/nativeCompile && jar -xvf *-SNAPSHOT.jar \
&& native-image -H:Name=application -J-Xmx5000m -Ob -march=compatibility $([[ -f META-INF/native-image/argfile ]] && echo @META-INF/native-image/argfile) -cp .:BOOT-INF/classes:$(ls -d -1 "$PWD/BOOT-INF/lib/"*.* | tr "\n" ":")'
