/*
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

@Component
@RegisterReflection(classes = {com.fasterxml.jackson.dataformat.xml.XmlMapper.class, com.azure.storage.blob.implementation.models.BlobStorageExceptionInternal.class, com.azure.storage.blob.implementation.models.BlobStorageError.class},
        memberCategories = {MemberCategory.DECLARED_CLASSES, MemberCategory.DECLARED_FIELDS, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS})

public class ObjectStorageAdapterAzure {

    private BlobServiceClient blobServiceClient;
    private final String schemaPrefix;

    public ObjectStorageAdapterAzure(
            @Value("${blob.storage.implementation}") String blobStorageImplementation,
            @Value("${multi-tenancy.schema-prefix:}") String schemaPrefix,
            @Value("${azure.storage.url}") String url,
            @Value("${azure.storage.account-name}") String accountName,
            @Value("${azure.storage.account-key}") String accountKey) {

        this.schemaPrefix = schemaPrefix;
        blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(String.format(url, accountName))
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();
    }

    public ObjectEntry getByKey(String key) {
        var outputStream = new ByteArrayOutputStream();
        var client = blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(key);
        client.downloadStream(outputStream);
        return new ObjectEntry(key, client.getProperties().getContentType(), (long) outputStream.toByteArray().length, outputStream.toByteArray());
    }

    public void deleteByKey(String key) {
        var client = blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(key);
        client.delete();
    }

    public void put(ObjectEntry objectEntry) {
        createBlobContainerIfNotExists();
        blobServiceClient.getBlobContainerClient(getBucketName());
        blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(objectEntry.objectName())
            .upload(new ByteArrayInputStream(objectEntry.data()), true);
    }

    private void createBlobContainerIfNotExists() {
        blobServiceClient.createBlobContainerIfNotExists(getBucketName());
        //Versioning is not support here, it has to be activated per Storage Account with Terraform: https://learn.microsoft.com/en-us/azure/storage/blobs/versioning-enable?tabs=portal
        //Lifecycles are also not supported here, activate per Storage Account with Terraform: https://learn.microsoft.com/en-us/azure/storage/blobs/lifecycle-management-policy-configure?source=recommendations&tabs=azure-portal
    }


    private String getBucketName() {
        return schemaPrefix.replaceAll("_", "-") + "0"; //TenantContext.getTenantId();
    }
}

*/
