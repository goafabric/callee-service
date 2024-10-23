package org.goafabric.calleeservice.s3;


import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
@Profile("azure") //wont be switchable with native images
public class ObjectStorageLogicAzure implements ObjectStorageLogic{

    private final BlobServiceClient blobServiceClient;

    public ObjectStorageLogicAzure(
            @Value("${azure.storage.url}") String url,
            @Value("${azure.storage.account-name}") String accountName,
            @Value("${azure.storage.account-key}") String accountKey) {

        blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(String.format(url, accountName))
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();
    }

    public ObjectEntry getById(String key) {
        var outputStream = new ByteArrayOutputStream();
        var client = blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(key);
        client.downloadStream(outputStream);
        return new ObjectEntry(key, client.getProperties().getContentType(), (long) outputStream.toByteArray().length, outputStream.toByteArray());
    }

    public void save(ObjectEntry objectEntry) {
        blobServiceClient.createBlobContainerIfNotExists(getBucketName());
        blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(objectEntry.objectName())
            .upload(new ByteArrayInputStream(objectEntry.data()), true);
    }

    public List<ObjectEntry> search(String search) {
        return blobServiceClient.getBlobContainerClient(getBucketName()).listBlobs().stream()
                .filter(item -> item.getName().toLowerCase().startsWith(search))
                .map(item -> getById(item.getName()))
                .toList();
    }

    public String getBucketName() {
        return "tenant-5";
    }

    @PostConstruct
    public void demo() {
        save(new ObjectEntry("hello_world.txt", "text/plain", Long.valueOf("hello world".length()), "hello world".getBytes()));
        System.err.println("getById : " + getById("hello_world.txt"));
        search("hello").stream().forEach(s -> System.err.println("fromlist : " + s.toString()));
    }
}

