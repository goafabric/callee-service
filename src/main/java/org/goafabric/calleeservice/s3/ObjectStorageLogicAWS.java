package org.goafabric.calleeservice.s3;

import io.awspring.cloud.autoconfigure.core.AwsAutoConfiguration;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Component
@Import(AwsAutoConfiguration.class)
public class ObjectStorageLogicAWS {

    private final S3Client s3Client;

    private String schemaPrefix;

    public ObjectStorageLogicAWS(S3Client s3Client, @Value("${multi-tenancy.schema-prefix:}") String schemaPrefix) {
        this.s3Client = s3Client;
        this.schemaPrefix = schemaPrefix;
    }

    @PostConstruct
    public void demo() {
        save(
                new ObjectEntry("hello_world.txt", "text/plain",
                        Long.valueOf("hello world".length()), "hello world".getBytes()));

        var objectEntry = getById("hello_world.txt");
        System.err.println("getById : " + objectEntry);
        search("hello").stream().forEach(s -> System.err.println("fromlist : " + s.toString()));
    }

    public void save(ObjectEntry objectEntry) {
        createBucketIfNotExists(getBucketName());
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(getBucketName())
                        .key(objectEntry.objectName())
                        .contentLength(objectEntry.objectSize())
                        .contentType(objectEntry.contentType())
                        .build(),
                RequestBody.fromBytes(objectEntry.data()));
    }

    public ObjectEntry getById(String id) {
        var response = s3Client.getObject(request -> request.bucket(getBucketName()).key(id));
        try {
            return new ObjectEntry(id, response.response().contentType(), response.response().contentLength(), response.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ObjectEntry> search(String search) {
        var contents = s3Client.listObjects(builder -> builder.bucket(getBucketName())).contents();
        return contents.stream().map(c ->
                new ObjectEntry(c.key(), null, c.size(), null))
                .filter(o -> o.objectName().toLowerCase().startsWith(search.toLowerCase()))
                .toList();
    }

    private void createBucketIfNotExists(String bucket) {
        if (s3Client.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucket))) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            s3Client.putBucketVersioning(PutBucketVersioningRequest.builder().bucket(bucket)
                    .versioningConfiguration(VersioningConfiguration.builder().status(BucketVersioningStatus.ENABLED).build())
                    .build());
        }
    }

    private String getBucketName() {
        return "console";
    }


}

