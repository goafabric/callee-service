package org.goafabric.calleeservice.s3;


import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
@RegisterReflection(classes = {com.fasterxml.jackson.dataformat.xml.XmlMapper.class, com.azure.storage.blob.implementation.models.BlobStorageExceptionInternal.class},
        memberCategories = {MemberCategory.DECLARED_CLASSES, MemberCategory.DECLARED_FIELDS, MemberCategory.INVOKE_DECLARED_METHODS})

public class ObjectStorageLogicAzure {

    private BlobServiceClient blobServiceClient;
    private final String schemaPrefix;

    public ObjectStorageLogicAzure(
            @Value("${blob.storage.implementation}") String blobStorageImplementation,
            @Value("${multi-tenancy.schema-prefix:}") String schemaPrefix,
            @Value("${azure.storage.url}") String url,
            @Value("${azure.storage.account-name}") String accountName,
            @Value("${azure.storage.account-key}") String accountKey) {

        this.schemaPrefix = schemaPrefix;
        if (blobStorageImplementation.equals("azure")) {
            blobServiceClient = new BlobServiceClientBuilder()
                    .endpoint(String.format(url, accountName))
                    .credential(new StorageSharedKeyCredential(accountName, accountKey))
                    .buildClient();
        }
    }

    public ObjectEntry getById(String key) {
        var outputStream = new ByteArrayOutputStream();
        var client = blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(key);
        client.downloadStream(outputStream);
        return new ObjectEntry(key, client.getProperties().getContentType(), (long) outputStream.toByteArray().length, outputStream.toByteArray());
    }

    public void save(ObjectEntry objectEntry) {
        try { blobServiceClient.createBlobContainer(getBucketName()); } catch (Exception e) {} //ifnotexists does not work in native mode
        blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(objectEntry.objectName())
            .upload(new ByteArrayInputStream(objectEntry.data()), true);
    }

    public List<ObjectEntry> search(String search) {
        return blobServiceClient.getBlobContainerClient(getBucketName()).listBlobs().stream()
                .filter(item -> item.getName().toLowerCase().startsWith(search))
                .map(item -> getById(item.getName()))
                .toList();
    }

    private String getBucketName() {
        return schemaPrefix.replaceAll("_", "-") + "0"; //TenantContext.getTenantId();
    }
}

