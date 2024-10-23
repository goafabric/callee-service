package org.goafabric.calleeservice.s3;


import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ObjectStorageAzure {

    private final String accountName;
    private final String accountKey;

    public ObjectStorageAzure(@Value("${azure.storage.account-name}") String accountName,
                              @Value("${azure.storage.account-key}") String accountKey) {
        this.accountName = accountName;
        this.accountKey = accountKey;
    }

    public ObjectEntry getById(String key) {
        var outputStream = new ByteArrayOutputStream();
        getBobClient().getBlobClient(key).downloadStream(outputStream);
        return new ObjectEntry(key, "TODO", (long) outputStream.toByteArray().length, outputStream.toByteArray());
    }

    public void save(ObjectEntry objectEntry) {
        getBobClient().getServiceClient().createBlobContainerIfNotExists(getBucketName());
        getBobClient().getBlobClient(objectEntry.objectName())
            .upload(new ByteArrayInputStream(objectEntry.data()), true);
    }

    public List<ObjectEntry> search(String search) {
        return getBobClient().listBlobs().stream()
                .filter(item -> item.getName().toLowerCase().startsWith(search))
                .map(item -> getById(item.getName()))
                .toList();
    }


    @PostConstruct
    public void demo() {
        save(new ObjectEntry("hello_world.txt", "text/plain", Long.valueOf("hello world".length()), "hello world".getBytes()));
        System.err.println("getById : " + getById("hello_world.txt"));
        search("hello").stream().forEach(s -> System.err.println("fromlist : " + s.toString()));
    }

    public BlobContainerClient getBobClient() {
        return new BlobContainerClientBuilder()
                .endpoint(String.format("https://%s.blob.core.windows.net", accountName))
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .containerName(getBucketName())
                .buildClient();
    }

    public String getBucketName() {
        return "tenant-5";
    }
}

