package dev.gaurav.dynamodbdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Blog {
    // --- KEYS ---
    // We do NOT put @Getter here because we need to manually annotate the method below.
    private String pk;
    private String sk;

    // --- DATA FIELDS ---
    // Lombok handles all getters/setters for these automatically
    private String title;
    private String content;
    private String author;
    private String commentText;
    private String creationDate;

    // --- MANUAL OVERRIDES FOR KEYS ---

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK") // Renames the column in DynamoDB to "PK"
    public String getPk() {
        return this.pk;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK") // Renames the column in DynamoDB to "SK"
    public String getSk() {
        return this.sk;
    }
}
