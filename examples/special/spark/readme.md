# dependency

implementation("org.apache.spark:spark-core_2.13:3.4.0") {exclude("org.apache.logging.log4j", "log4j-slf4j2-impl")}

tasks.withType<JavaExec> { jvmArgs("--add-opens=java.base/sun.nio.ch=ALL-UNNAMED") }

# deploy
./spark-submit -v --class org.goafabric.calleeservice.Application --master local \
/Users/andreas/IdeaProjects/private/callee-service/build/libs/callee-service-3.0.5-spark-SNAPSHOT.jar > yo.txt && cat ./yo.txt

# docker
docker run --name spark --rm -it apache/spark:3.4.0 /opt/spark/bin/spark-shell

# links
https://www.baeldung.com/apache-spark
