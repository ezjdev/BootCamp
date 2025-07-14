package com.homework26.api;

import com.amazonaws.services.s3.model.S3Object;

import java.util.List;
import java.util.Optional;

public interface S3FileManager {

    void saveToS3(String bucket, byte[] fileBytes, String fileName, String contentType);

    Optional<S3Object> getS3Object(String bucket, String objectKey);

    void deleteObject(String bucket, String objectKey);

    List<String> listFiles();
}
