FROM ghcr.io/graalvm/graalvm-ce:ol9-java17-22.3.2

RUN gu install native-image && \
    mkdir -p /home/project

ADD ["buildNativeImage.sh", "/home/project/buildNativeImage.sh"]

RUN chmod +x /home/project/buildNativeImage.sh