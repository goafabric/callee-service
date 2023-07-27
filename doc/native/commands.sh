gradle clean bootJar

docker build -t native-image-builder ./doc/native && \
docker run --rm --name native-image-builder --mount type=bind,source=$(pwd)/build,target=/home/project/build \
native-image-builder /bin/bash -c "/home/project/buildNativeImage.sh"

gradle jibNativeImage

#docker run --rm --name native-image-builder --mount type=bind,source=$(pwd)/build,target=/home/project/build \
#native-image-builder /bin/bash -c 'mkdir -p /home/project/build/native/nativeCompile && cp /home/project/build/libs/*-SNAPSHOT.jar /home/project/build/native/nativeCompile && cd /home/project/build/native/nativeCompile && jar -xvf *-SNAPSHOT.jar \
#&& [[ -f @META-INF/native-image/argfile ]] && echo !!using ARGFILE && ARGFILE=@META-INF/native-image/argfile; native-image -H:Name=myapplication -Ob $ARGFILE -cp .:BOOT-INF/classes:$(find BOOT-INF/lib | tr "\n" ":")'
