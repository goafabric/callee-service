/*
package org.goafabric.calleeservice.s3;

import io.awspring.cloud.autoconfigure.core.AwsAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Component
@Import(AwsAutoConfiguration.class)
public class ObjectStorageAdapterAWS {

    private final S3Client s3Client;

    private String schemaPrefix;

    public ObjectStorageAdapterAWS(S3Client s3Client, @Value("${multi-tenancy.schema-prefix:}") String schemaPrefix) {
        this.s3Client = s3Client;
        this.schemaPrefix = schemaPrefix;
    }

    public ObjectEntry getByKey(String key) {
        var response = s3Client.getObject(request -> request.bucket(getBucketName()).key(key));
        try {
            return new ObjectEntry(key, response.response().contentType(), response.response().contentLength(), response.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteByKey(String key) {
        s3Client.deleteObject(request -> request.bucket(getBucketName()).key(key));
    }

    public void put(ObjectEntry objectEntry) {
        createBucketIfNotExists(getBucketName());
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(getBucketName())
                        .key(objectEntry.objectName())
                        .contentLength(objectEntry.objectSize())
                        .contentType(objectEntry.contentType())
                        .build(),
                RequestBody.fromBytes(objectEntry.data()));
    }

    private void createBucketIfNotExists(String bucket) {
        if (!existsBucket(bucket)) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            // Not Supported by Azure Blob Storage, so left out here
            //s3Client.putBucketVersioning(PutBucketVersioningRequest.builder().bucket(bucket)
            //        .versioningConfiguration(VersioningConfiguration.builder().status(BucketVersioningStatus.ENABLED).build())
            //        .build());
        }
    }

    private boolean existsBucket(String bucket) {
        try {
            s3Client.headBucket(request -> request.bucket(bucket));
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }

    private String getBucketName() {
        return "console";
    }


}

*/
