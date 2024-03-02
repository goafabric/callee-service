//package org.goafabric.calleeservice.s3;
//
//import am.ik.s3.ListBucketResult;
//import am.ik.s3.ListBucketsResult;
//import am.ik.s3.S3Client;
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
//@RegisterReflectionForBinding({ListBucketResult.class, ListBucketsResult.class}) //implementation("am.ik.s3:simple-s3-client:0.1.1") {exclude("org.springframework", "spring-web")}
//public class ObjectStorageLogicNativeHack {
//
//    private final S3Client s3Client;
//
//    private String schemaPrefix;
//
//    private ThreadLocal<String> contentType = new ThreadLocal<>();
//
//    public ObjectStorageLogicNativeHack(@Value("${multi-tenancy.schema-prefix:}") String schemaPrefix,
//                                        @Value("${spring.cloud.aws.s3.endpoint}") String endPoint,
//                                        @Value("${spring.cloud.aws.region.static}") String region,
//                                        @Value("${spring.cloud.aws.credentials.access-key}") String accessKey,
//                                        @Value("${spring.cloud.aws.credentials.secret-key}") String secretKey) {
//        var restTemplate = new RestTemplate();
//        this.s3Client = new S3Client(restTemplate, URI.create(endPoint), region, accessKey, secretKey);
//        this.schemaPrefix = schemaPrefix;
//
//        restTemplate.getInterceptors().add((request, body, execution) -> {
//            var response = execution.execute(request, body);
//            contentType.set(response.getHeaders().getFirst("Content-Type"));
//            return response;
//        });
//
//    }
//
//    @PostConstruct
//    public void demo() {
//        save(
//                new ObjectEntry("hello_world.txt", "text/plain",
//                        Long.valueOf("hello world".length()), "hello world".getBytes()));
//        System.err.println("getById : " + getById("hello_world.txt"));
//        System.err.println("getById2 : " + getById("hello_world.txt"));
//        //search("hello").stream().forEach(s -> System.err.println("fromlist : " + s.toString()));
//
//    }
//
//    public void save(ObjectEntry objectEntry) {
//        createBucketIfNotExists(getBucketName());
//        s3Client.putObject(getBucketName(), objectEntry.objectName(), objectEntry.data(),
//                MediaType.valueOf(objectEntry.contentType));
//    }
//
//    public ObjectEntry getById(String id) {
//        var data = s3Client.getObject(getBucketName(), id);
//        return new ObjectEntry(id, contentType.get(), (long) data.length, data);
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
