/*
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

    public void uploadFile(String fileName, byte[] fileData) {
        blobContainerClient.getBlobClient(fileName)
            .upload(new ByteArrayInputStream(fileData), fileData.length);
    }

    public byte[] downloadFile(String fileName) {
        var outputStream = new ByteArrayOutputStream();
        blobContainerClient.getBlobClient(fileName).download(outputStream);
        return outputStream.toByteArray();
    }

    public void deleteFile(String fileName) {
        blobContainerClient.getBlobClient(fileName).delete();
    }

    @PostConstruct
    public void demo() {
        System.out.println(downloadFile("Cletus.png"));
    }

    public String getBucketName() {
        return "mybucket";
    }
}

 */

