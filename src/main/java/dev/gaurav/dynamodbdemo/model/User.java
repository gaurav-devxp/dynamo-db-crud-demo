package dev.gaurav.dynamodbdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class User {
    private String id;
    private String keycloakId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role = UserRole.USER;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    // We can add a GSI for email if we want to enforce uniqueness or efficient
    // lookups by email
    // but for now we'll stick to basic fields.
    // To properly enforce uniqueness in DynamoDB, we would normally use a separate
    // table or a transaction with a condition check,
    // or use the email as the PK.
    // For this demo, let's treat 'email' as a secondary index if we need to query
    // by it.

    @DynamoDbSecondaryPartitionKey(indexNames = "email-index")
    public String getEmail() {
        return email;
    }
}
