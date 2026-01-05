package dev.gaurav.dynamodbdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SqsConfig {
    @Value("${aws.dynamodb.endpoint}") // We can reuse the localhost:4566 endpoint
    private String localStackEndpoint;

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create(localStackEndpoint)) // Forces connection to localhost:4566
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        // This explicitly forces 'test/test' and ignores any
                        // AWS_SESSION_TOKEN in your environment vars
                        AwsBasicCredentials.create("test", "test")
                ))
                .build();
    }
}
