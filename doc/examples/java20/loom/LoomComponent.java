package org.goafabric.calleeservice.loom;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

//https://www.marcobehler.com/guides/java-project-loom
@Component
@RestController
public class LoomComponent implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        run();
    }

    public void run() {
        final long startTime = System.currentTimeMillis();
        doSleep(1000, 1, false);
        //doFile(1000, 2, false);
        //doHttp(100, 2, false);
        System.err.println("## process took : " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static void doSleep(int threadCount, int rounds, boolean virtual) {
        for (int round = 0; round < rounds; round++) {
            try (var executor = virtual ? Executors.newVirtualThreadPerTaskExecutor() : Executors.newFixedThreadPool(threadCount)) {
                IntStream.range(0, threadCount).forEach(i -> executor.submit(() -> {  // (1)
                    Thread.sleep(Duration.ofSeconds(1));
                    System.out.println("iteration : " + i + ", active threads : " + Thread.activeCount());
                    return i;
                }));
            }
        }
    }


    private static void doFile(int threadCount, int rounds, boolean virtual) {
        for (int round = 0; round < rounds; round++) {
            try (var executor = virtual ? Executors.newVirtualThreadPerTaskExecutor() : Executors.newFixedThreadPool(threadCount)) {
                IntStream.range(0, threadCount).forEach(i -> executor.submit(() -> {  // (1)
                    readFile(i);
                    System.out.println("iteration : " + i + ", active threads : " + Thread.activeCount());
                    return i;
                }));
            }
        }
    }


    private static void doHttp(int threadCount, int rounds, boolean virtual) {
        for (int round = 0; round < rounds; round++) {
            try (var executor = virtual ? Executors.newVirtualThreadPerTaskExecutor() : Executors.newFixedThreadPool(threadCount)) {
                IntStream.range(0, threadCount).forEach(i -> executor.submit(() -> {  // (1)
                    var response = getURL("http://localhost:50900/callees/sayMyName");
                    System.out.println("iteration : " + i + ", active threads : " + Thread.activeCount()
                            + ", response: " + response);
                    return i;
                }));
            }
        }
    }


    private static void readFile(int i) {
        final String pathSource;
        try {
            pathSource = ResourceUtils.getURL("classpath:testfiles") + "/";
            final byte[] data = Files.readAllBytes(ResourceUtils.getFile(pathSource + "big.pdf").toPath());
            //Files.write(ResourceUtils.getFile(ResourceUtils.getURL("file:/Users/andreas/Downloads") + "/" + "big" + i + ".pdf").toPath(), data);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private static String getURL(String url) {
        try (InputStream in = new URL(url).openStream()) {
            byte[] bytes = in.readAllBytes(); // ALERT, ALERT!
            final String page = new String(bytes);
            return page;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/callees/sayMyName", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sayMyName () {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Your name is Heisenberg";
    }



}
