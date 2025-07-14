package com.homework26.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.homework26.api.S3FileManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableConfigurationProperties(AwsS3Properties.class)
public class AwsS3Configuration implements S3FileManager {

    @Getter
    private final String bucketName;

    private final TransferManager transferManager;

    AwsS3Configuration(AwsS3Properties awsS3Properties) {
        final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withClientConfiguration(clientConfiguration(awsS3Properties))
                .withPathStyleAccessEnabled(true) // disable virtual host style connection and enable path style s3 bucket
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsS3Properties.getCredentials().getAccessKey(), awsS3Properties.getCredentials().getSecretKey())))
                .withEndpointConfiguration(new EndpointConfiguration(awsS3Properties.getS3().getEndpoint(), awsS3Properties.getRegion().getStatic()))
                .build();
        final Supplier<List<Bucket>> buckets = lazy(amazonS3::listBuckets);
        this.bucketName = Optional.ofNullable(awsS3Properties.getS3().getBucket())
                .or(() -> buckets.get().stream().findFirst().map(Bucket::getName))
                .orElseThrow(() -> new IllegalStateException("Firstly you should create bucket for uploads in S3"));
        log.info("""
                        
                        #####  S3 Settings  #####
                        Owner: {},
                        Buckets: {},
                        Used bucket: {},
                        Region: {}
                        """,
                amazonS3.getS3AccountOwner().getDisplayName(),
                buckets.get().stream().map(Bucket::getName).toList(),
                this.bucketName,
                amazonS3.getRegionName()
        );
        this.transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
        log.info("""
                        
                        #####  TransferManager configuration  #####
                        MinimumUploadPartSize: {}
                        MultipartCopyThreshold: {}
                        MultipartCopyPartSize: {}
                        """,
                transferManager.getConfiguration().getMinimumUploadPartSize(),
                transferManager.getConfiguration().getMultipartCopyThreshold(),
                transferManager.getConfiguration().getMultipartCopyPartSize()
        );
    }

    @Override
    public void saveToS3(String bucket, byte[] fileBytes, String fileName, String contentType) {
        try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            final ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(fileBytes.length);
            objectMetadata.setContentType(contentType);
            objectMetadata.setExpirationTime(DateUtils.addDays(new Date(), 1));
            objectMetadata.setContentEncoding(StandardCharsets.UTF_8.name());
            final String targetBucketName = getBucketName(bucket);
            transferManager.upload(
                    targetBucketName,
                    fileName,
                    inputStream,
                    objectMetadata).waitForCompletion();
            log.info("Uploaded file to S3, bucket: {}, file name: {}", targetBucketName, fileName);
        } catch (IOException | InterruptedException e) {
            log.error("Unable to save file to S3, bucket: {}, file name: {}", bucket, fileName, e);
        }
    }

    @Override
    public Optional<S3Object> getS3Object(String bucket, String objectKey) {
        final String targetBucketName = getBucketName(bucket);
        if (!transferManager.getAmazonS3Client().doesObjectExist(targetBucketName, objectKey)) {
            return Optional.empty();
        }
        final S3Object s3Object = transferManager.getAmazonS3Client().getObject(targetBucketName, objectKey);
        log.info("S3 Object: {} in bucket: {}", s3Object.getKey(), targetBucketName);
        return Optional.of(s3Object);
    }

    @Override
    public void deleteObject(String bucket, String objectKey) {
        final String targetBucketName = getBucketName(bucket);
        try {
            transferManager.getAmazonS3Client().deleteObject(targetBucketName, objectKey);
            log.info("S3 Object successfully deleted: {} in bucket: {}", objectKey, targetBucketName);
        } catch (AmazonS3Exception e) {
            log.error("Failed during S3 Object deletion: {} in bucket: {}", objectKey, targetBucketName, e);
        }
    }

    @Override
    public List<String> listFiles() {
        ListObjectsV2Result result = transferManager.getAmazonS3Client().listObjectsV2(bucketName);
        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .toList();
    }

    private String getBucketName(String bucket) {
        return Objects.toString(bucket, this.bucketName);
    }

    private static ClientConfiguration clientConfiguration(AwsS3Properties awsS3Properties) {
        return new ClientConfiguration();
    }

    private static <T> Supplier<T> lazy(final Supplier<T> supplier) {
        return new Supplier<>() {

            private T value;

            @Override
            public T get() {
                return (T) ObjectUtils.defaultIfNull(value, this.value = supplier.get());
            }
        };
    }
}
