package dev.gaurav.dynamodbdemo.repository;

import dev.gaurav.dynamodbdemo.model.Blog;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BlogRepository {
    private final DynamoDbTable<Blog> table;

    @PostConstruct
    public void createTableIfNotExists() {
        try {
            // Attempt to create the table
            table.createTable(CreateTableEnhancedRequest.builder()
                    // If using real AWS, you pay for throughput.
                    // For Local/Dev, these numbers don't matter much.
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build());
            System.out.println("✅ Table 'BlogTable' created successfully.");
        } catch (Exception e) {
            // If table already exists, the SDK throws an exception. We ignore it.
            if (e.getMessage().contains("Table already exists")) {
                System.out.println("ℹ️ Table 'BlogTable' already exists.");
            } else {
                throw e; // Rethrow real errors
            }
        }
    }

    public BlogRepository(DynamoDbEnhancedClient enhancedClient) {
        // Maps the class to the actual DynamoDB table named "Blog"
        this.table = enhancedClient.table("Blog", TableSchema.fromBean(Blog.class));
    }

    // Save any item (Post OR Comment)
    public void save(Blog entity) {
        table.putItem(entity);
    }

    // FETCH EVERYTHING: Get Post + All Comments in ONE request
    public List<Blog> getPostWithComments(String postId) {
        // Query: PK = "POST#{id}"
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue("POST#" + postId).build()
        );

        // This returns a list containing 1 Post (META) and N Comments
        return table.query(queryConditional).items().stream().collect(Collectors.toList());
    }
}
