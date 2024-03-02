//package org.goafabric.calleeservice.s3;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//import java.util.List;
//
//@Component
//@RegisterReflectionForBinding({S3ClientAmIk.ListBucketResult.class, S3ClientAmIk.ListBucketsResult.class})
//public class ObjectStorageLogicNative {
//
//    private final S3ClientAmIk s3Client;
//
//    private String schemaPrefix;
//
//    public ObjectStorageLogicNative(@Value("${multi-tenancy.schema-prefix:}") String schemaPrefix,
//                                    @Value("${spring.cloud.aws.s3.endpoint}") String endPoint,
//                                    @Value("${spring.cloud.aws.region.static}") String region,
//                                    @Value("${spring.cloud.aws.credentials.access-key}") String accessKey,
//                                    @Value("${spring.cloud.aws.credentials.secret-key}") String secretKey) {
//        this.s3Client = new S3ClientAmIk(new RestTemplate(), URI.create(endPoint), region, accessKey, secretKey);
//        this.schemaPrefix = schemaPrefix;
//    }
//
//    @PostConstruct
//    public void demo() {
//        save(
//                new ObjectEntry("hello_world.txt", "text/plain",
//                        Long.valueOf("hello world".length()), "hello world".getBytes()));
//        var objectEntry = getById("hello_world.txt");
//        System.err.println("getById : " + objectEntry);
//        search("hello").stream().forEach(s -> System.err.println("fromlist : " + s.toString()));
//    }
//
//    public void save(ObjectEntry objectEntry) {
//        createBucketIfNotExists(getBucketName());
//        s3Client.putObject(getBucketName(), objectEntry.objectName(), objectEntry.data(),
//                MediaType.valueOf(objectEntry.contentType));
//    }
//
//    public ObjectEntry getById(String id) {
//        //var data = s3Client.getObject(getBucketName(), id);
//        var response = s3Client.getObjectAndMetadata(getBucketName(), id);
//        var data = response.getBody();
//        return new ObjectEntry(id, response.getHeaders().getFirst("Content-Type"), (long) data.length, data);
//    }
//
//    public List<ObjectEntry> search(String search) {
//        var bucketResult = s3Client.listBucket(getBucketName());
//        return bucketResult.contents().stream().map(c ->
//                        new ObjectEntry(c.key(), null, c.size(), null))
//                .filter(o -> o.objectName().toLowerCase().startsWith(search.toLowerCase()))
//                .toList();
//    }
//
//    private void createBucketIfNotExists(String bucket) {
//        if (s3Client.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucket))) { //this could be slow
//            s3Client.putBucket(getBucketName());
//            //Bucket Versioning is missing here
//        }
//    }
//
//    private String getBucketName() {
//        return "console";
//    }
//
//    public record ObjectEntry(
//            String objectName,
//            String contentType,
//            Long objectSize,
//            byte[] data)
//    {}
//}
//
//
