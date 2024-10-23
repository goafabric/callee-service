package org.goafabric.calleeservice.s3;


import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class ObjectStorageAzure {

    private final BlobContainerClient blobContainerClient;

    public ObjectStorageAzure(@Value("${azure.storage.account-name}") String accountName,
                              @Value("${azure.storage.account-key}") String accountKey) {
        String endpoint = String.format("https://%s.blob.core.windows.net", accountName);
        this.blobContainerClient = new BlobContainerClientBuilder()
                .endpoint(endpoint)
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .containerName(getBucketName())
                .buildClient();
    }

    public ObjectEntry getById(String key) {
        var outputStream = new ByteArrayOutputStream();
        blobContainerClient.getBlobClient(key).downloadStream(outputStream);
        return new ObjectEntry(key, "TODO", (long) outputStream.toByteArray().length, outputStream.toByteArray());
    }

    public void save(ObjectEntry objectEntry) {
        blobContainerClient.getServiceClient().createBlobContainerIfNotExists(getBucketName());
        blobContainerClient.getBlobClient(objectEntry.objectName())
            .upload(new ByteArrayInputStream(objectEntry.data()), objectEntry.objectSize());
    }

    @PostConstruct
    public void demo() {
        System.out.println(getById("Cletus.png"));
    }

    //    @PostConstruct
//    public void demo() {
//        save(
//                new ObjectEntry("hello_world.txt", "text/plain",
//                        Long.valueOf("hello world".length()), "hello world".getBytes()));
//        var objectEntry = getById("hello_world.txt");
//        System.err.println("getById : " + objectEntry);
//        search("hello").stream().forEach(s -> System.err.println("fromlist : " + s.toString()));
//    }


    public String getBucketName() {
        return "mybucket";
    }
}

