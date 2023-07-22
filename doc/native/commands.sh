gradle bootJar

docker build -t native-image-builder ./doc/native && \
docker run --rm --name native-image-builder --mount type=bind,source=$(pwd)/build,target=/home/project/build \
native-image-builder /bin/bash -c "/home/project/buildNativeImage.sh"
