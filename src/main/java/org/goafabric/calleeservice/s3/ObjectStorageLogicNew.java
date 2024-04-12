package org.goafabric.calleeservice.s3;

import am.ik.s3.ListBucketResult;
import am.ik.s3.ListBucketsResult;
import am.ik.s3.S3Content;
import am.ik.s3.S3RequestBuilders;
import jakarta.annotation.PostConstruct;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static am.ik.s3.S3RequestBuilder.s3Request;

@Component
@RegisterReflectionForBinding({ListBucketResult.class, ListBucketsResult.class}) //implementation("am.ik.s3:simple-s3-client:0.1.1") {exclude("org.springframework", "spring-web")}
public class ObjectStorageLogicNew {

    private final Boolean    s3Enabled;
    private final String     schemaPrefix;
    private final RestClient restClient;
    private final String     endPoint;
    private final String     region;
    private final String     accessKey;
    private final String     secretKey;
    private final List<ObjectEntry> objectEntriesInMem = new ArrayList<>();

    public ObjectStorageLogicNew(@Value("${spring.cloud.aws.s3.enabled}") Boolean s3Enabled,
                                 @Value("${multi-tenancy.schema-prefix:}") String schemaPrefix,
                                 @Value("${spring.cloud.aws.s3.endpoint}") String endPoint,
                                 @Value("${spring.cloud.aws.region.static}") String region,
                                 @Value("${spring.cloud.aws.credentials.access-key}") String accessKey,
                                 @Value("${spring.cloud.aws.credentials.secret-key}") String secretKey) {
        this.s3Enabled = s3Enabled;
        this.schemaPrefix = schemaPrefix;
        this.restClient = RestClient.builder().messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2XmlHttpMessageConverter())).build();
        this.endPoint = endPoint;
        this.region = region;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }


    public ObjectEntry getById(String id) {
        if (!s3Enabled) { return objectEntriesInMem.stream().filter(o -> o.objectName().equals(id)).findFirst().get(); }

        var request = s3RequestPath(HttpMethod.GET, id).build();
        var response = restClient.get().uri(request.uri()).headers(request.headers()).retrieve().toEntity(byte[].class);
        return new ObjectEntry(id, response.getHeaders().getFirst("Content-Type"), (long) response.getBody().length, response.getBody());
    }

    public List<ObjectEntry> search(String search) {
        if (!s3Enabled) { return objectEntriesInMem.stream().filter(o -> o.objectName().startsWith(search)).toList(); }

        var request = s3RequestPath(HttpMethod.GET, null).build();
        var response = restClient.get().uri(request.uri()).headers(request.headers()).retrieve()
                .toEntity(ListBucketResult.class).getBody();

        return response.contents().stream().map(c ->
                        new ObjectEntry(c.key(), null, c.size(), null))
                .filter(o -> o.objectName().toLowerCase().startsWith(search.toLowerCase()))
                .toList();
    }

    public void save(ObjectEntry objectEntry) {
        if (!s3Enabled) { objectEntriesInMem.add(objectEntry); return; }

        createBucketIfNotExists(getBucketName());

        var request = s3RequestPath(HttpMethod.PUT, objectEntry.objectName())
                .content(S3Content.of(objectEntry.data(), MediaType.valueOf(objectEntry.contentType())))
                .build();

        restClient.put().uri(request.uri())
                .headers(request.headers())
                .body(objectEntry.data())
                .retrieve().toBodilessEntity();
    }

    private void createBucketIfNotExists(String bucket) {
        var request = s3Path(HttpMethod.GET).path(b -> b).build();

        var response = restClient.get().uri(request.uri()).headers(request.headers()).retrieve()
                .toEntity(ListBucketsResult.class).getBody();

        if (response.buckets().stream().noneMatch(b -> b.name().equals(bucket))) { //this could be slow
            var request2 = s3RequestPath(HttpMethod.PUT, null).build();
            restClient.put().uri(request2.uri()).headers(request2.headers()).retrieve().toBodilessEntity();
        }
    }

    private S3RequestBuilders.Optionals s3RequestPath(HttpMethod httpMethod, String key) {
        return s3Path(httpMethod).path(b -> b.bucket(getBucketName()).key(key));
    }

    private S3RequestBuilders.Path s3Path(HttpMethod httpMethod) {
        try {
            return s3Request().endpoint(new URI(endPoint))
                    .region(region)
                    .accessKeyId(accessKey)
                    .secretAccessKey(secretKey)
                    .method(httpMethod);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String getBucketName() {
        return "console";
        //return schemaPrefix.replaceAll("_", "-") + TenantContext.getTenantId();
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

}

