package org.goafabric.calleeservice;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.regex.Pattern;

public class SparkExamples {
    public static void numbers() {
        var conf = new SparkConf().setAppName("JavaNumbers").setMaster("local"); //("spark://localhost:7077");

        try (JavaSparkContext ctx = new JavaSparkContext(conf)) {
            JavaRDD<Integer> numbers = ctx.parallelize(Arrays.asList(3, 6, 9, 10, -5 , -7));
            int sum = numbers.map(Math::abs).reduce(Integer::sum);

            System.out.println("# Sum is: " + sum);
        }
    }

    public static void words() {
        final Pattern SPACE = Pattern.compile(" ");

        var conf = new SparkConf().setAppName("JavaNumbers").setMaster("local");
        try (JavaSparkContext ctx = new JavaSparkContext(conf)) {
            var list = Arrays.asList("Hello from Baeldung", "Keep learning Spark", "Bye from Baeldung");

            JavaRDD<String> lines = ctx.parallelize(list);

            var words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());
            var wordAsTuple = words.mapToPair(word -> new Tuple2<>(word, 1));
            var wordWithCount = wordAsTuple.reduceByKey(Integer::sum);
            var output = wordWithCount.collect();
            for (Tuple2<?, ?> tuple : output) {
                System.out.println(tuple._1() + ": " + tuple._2());
            }
            ctx.stop();
        }
    }
}
