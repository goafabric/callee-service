gradle clean bootJar

docker build -t native-image-builder ./doc/native

#docker run --rm --name native-image-builder --mount type=bind,source=$(pwd)/build,target=/build \
#native-image-builder /bin/bash -c "/buildNativeImage.sh"

docker run --rm --name native-image-builder --mount type=bind,source=$(pwd)/build,target=/build \
native-image-builder /bin/bash -c 'mkdir -p /build/native/nativeCompile && cp /build/libs/*-SNAPSHOT.jar /build/native/nativeCompile && cd /build/native/nativeCompile && jar -xvf *-SNAPSHOT.jar \
&& native-image -H:Name=application -Ob -march=compatibility $([[ -f META-INF/native-image/argfile ]] && echo @META-INF/native-image/argfile) -cp .:BOOT-INF/classes:$(find BOOT-INF/lib | tr "\n" ":")'


gradle jibNativeImage

