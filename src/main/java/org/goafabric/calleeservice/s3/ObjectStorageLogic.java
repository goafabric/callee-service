package org.goafabric.calleeservice.s3;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ObjectStorageLogic {
    private final String blobStorageImplementation;
    private final ObjectStorageLogicAWS objectStorageLogicAWS;
    private final ObjectStorageLogicAzure objectStorageLogicAzure;

    private final List<ObjectEntry> objectEntriesInMem = new ArrayList<>();

    public ObjectStorageLogic(
            @Value("${blob.storage.implementation}") String blobStorageImplementation,
            ObjectStorageLogicAWS objectStorageLogicAWS,
            ObjectStorageLogicAzure objectStorageLogicAzure) {
        this.blobStorageImplementation = blobStorageImplementation;
        this.objectStorageLogicAWS = objectStorageLogicAWS;
        this.objectStorageLogicAzure = objectStorageLogicAzure;
    }


    public ObjectEntry getById(String key) {
        return switch (blobStorageImplementation) {
            case "aws" -> objectStorageLogicAWS.getById(key);
            case "azure" -> objectStorageLogicAzure.getById(key);
            case "memory" -> objectEntriesInMem.stream().filter(o -> o.objectName().equals(key)).findFirst().get();
            default -> throw new IllegalStateException("unknown blob implementation");
        };
    }


    public List<ObjectEntry> search(String search) {
        return switch (blobStorageImplementation) {
            case "aws" -> objectStorageLogicAWS.search(search);
            case "azure" -> objectStorageLogicAzure.search(search);
            case "memory" -> objectEntriesInMem.stream().filter(o -> o.objectName().startsWith(search)).toList();
            default -> throw new IllegalStateException("unknown blob implementation");
        };
    }

    public void save(ObjectEntry objectEntry) {
        switch (blobStorageImplementation) {
            case "aws" -> objectStorageLogicAWS.save(objectEntry);
            case "azure" -> objectStorageLogicAzure.save(objectEntry);
            case "memory" -> objectEntriesInMem.add(objectEntry);
            default -> throw new IllegalStateException("unknown blob implementation");
        }
    }

    @PostConstruct
    public void demo() {
        try {
            save(new ObjectEntry("hello_world.txt", "text/plain", Long.valueOf("hello world".length()), "hello world".getBytes()));
            System.err.println("getById : " + getById("hello_world.txt"));
            search("hello").stream().forEach(s -> System.err.println("fromlist : " + s.toString()));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}

