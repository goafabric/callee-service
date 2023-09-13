gradle clean bootJar && \
docker run --rm --name native-image-builder --mount type=bind,source=$(pwd)/build,target=/build --entrypoint /bin/bash ghcr.io/graalvm/native-image-community:17.0.8 \
-c 'mkdir -p /build/native && cp /build/libs/*-SNAPSHOT.jar /build/native && cd /build/native && jar -xvf *-SNAPSHOT.jar \
&& native-image -H:Name=application -J-Xmx5000m -Ob -march=compatibility $([[ -f META-INF/native-image/argfile ]] && echo @META-INF/native-image/argfile) -cp .:BOOT-INF/classes:$(ls -d -1 "$PWD/BOOT-INF/lib/"*.* | tr "\n" ":")'

gradle jibNativeImage

docker run -it --mount type=bind,source=$(pwd)/build,target=/build --entrypoint /bin/bash ghcr.io/graalvm/native-image-community:17.0.8

docker run --rm --entrypoint /bin/bash ghcr.io/graalvm/native-image-community:17.0.8 -c "mkdir -p /build/native"