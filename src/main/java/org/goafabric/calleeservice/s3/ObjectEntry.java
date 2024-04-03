package org.goafabric.calleeservice.s3;

public record ObjectEntry(
    String objectName,
    String contentType,
    Long objectSize,
    byte[] data)
{}
