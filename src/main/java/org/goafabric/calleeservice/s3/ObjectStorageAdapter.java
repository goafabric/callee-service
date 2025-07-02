/*
package org.goafabric.calleeservice.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//gradle
//implementation("io.awspring.cloud:spring-cloud-aws-starter-s3:3.4.0");
//implementation("com.azure:azure-storage-blob:12.30.0"); implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
//
//graalvmNative {
//	binaries.named("main") {
//		buildArgs.add("--initialize-at-build-time=org.slf4j.helpers.Reporter") //required for azure blob from boot 3.3.3+
//	}
//}
// docker run --name azurite --rm -p 10000:10000 -p 10001:10001 -p 10002:10002 mcr.microsoft.com/azure-storage/azurite

@Component
public class ObjectStorageAdapter {
    private final String blobStorageImplementation;
    private final ObjectStorageAdapterAWS objectStorageAdapterAWS;
    private final ObjectStorageAdapterAzure objectStorageAdapterAzure;

    private final List<ObjectEntry> objectEntriesInMem = new ArrayList<>();

    public ObjectStorageAdapter(
            @Value("${blob.storage.implementation}") String blobStorageImplementation,
            ObjectStorageAdapterAWS objectStorageAdapterAWS,
            ObjectStorageAdapterAzure objectStorageAdapterAzure) {
        this.blobStorageImplementation = blobStorageImplementation;
        this.objectStorageAdapterAWS = objectStorageAdapterAWS;
        this.objectStorageAdapterAzure = objectStorageAdapterAzure;
    }


    public ObjectEntry getByKey(String key) {
        return switch (blobStorageImplementation) {
            case "aws" -> objectStorageAdapterAWS.getByKey(key);
            case "azure" -> objectStorageAdapterAzure.getByKey(key);
            case "memory" -> objectEntriesInMem.stream().filter(o -> o.objectName().equals(key)).findFirst().get();
            default -> throw new IllegalStateException("unknown blob implementation");
        };
    }

    public void deleteByKey(String key) {
        switch (blobStorageImplementation) {
            case "aws" -> objectStorageAdapterAWS.deleteByKey(key);
            case "azure" -> objectStorageAdapterAzure.deleteByKey(key);
            //case "memory" -> objectEntriesInMem.stream().filter(o -> o.objectName().equals(key)).findFirst().get();
            default -> throw new IllegalStateException("unknown blob implementation");
        };
    }

    public void put(ObjectEntry objectEntry) {
        switch (blobStorageImplementation) {
            case "aws" -> objectStorageAdapterAWS.put(objectEntry);
            case "azure" -> objectStorageAdapterAzure.put(objectEntry);
            case "memory" -> objectEntriesInMem.add(objectEntry);
            default -> throw new IllegalStateException("unknown blob implementation");
        }
    }

    @PostConstruct
    public void demo() {
        try {
            put(new ObjectEntry("hello_world.txt", "text/plain", Long.valueOf("hello world".length()), "hello world".getBytes()));
            System.err.println("getById : " + getByKey("hello_world.txt"));
            deleteByKey("hello_world.txt");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}

*/
