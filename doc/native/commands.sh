gradle clean bootJar

docker build -t native-image-builder ./doc/native

#docker run --rm --name native-image-builder --mount type=bind,source=$(pwd)/build,target=/home/build \
#native-image-builder /bin/bash -c "/home/buildNativeImage.sh"

docker run --rm --name native-image-builder --mount type=bind,source=$(pwd)/build,target=/home/build \
native-image-builder /bin/bash -c 'mkdir -p /home/build/native/nativeCompile && cp /home/build/libs/*-SNAPSHOT.jar /home/build/native/nativeCompile && cd /home/build/native/nativeCompile && jar -xvf *-SNAPSHOT.jar \
&& [[ -f @META-INF/native-image/argfile ]] && echo !!using ARGFILE && ARGFILE=@META-INF/native-image/argfile; native-image -H:Name=application -Ob $ARGFILE -cp .:BOOT-INF/classes:$(find BOOT-INF/lib | tr "\n" ":")'


gradle jibNativeImage

