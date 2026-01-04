package dev.gaurav.dynamodbdemo.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import dev.gaurav.dynamodbdemo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepository {
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    @PostConstruct
    public void init() {
        this.userTable = dynamoDbEnhancedClient.table("users", TableSchema.fromBean(User.class));
        // In a real production app, table creation is usually handled via IaC
        // (Terraform, CDK, etc.)
        // For this demo, we'll try to create it if it doesn't exist.
        try {
            this.userTable.createTable();
        } catch (Exception e) {
            // Table already exists or other issue - ignoring for demo
            System.err.println("Table creation failed (might already exist): " + e.getMessage());
        }
    }

    public void save(User user) {
        userTable.putItem(user);
    }

    public Optional<User> getUser(String id) {
        return Optional.ofNullable(userTable.getItem(r -> r.key(k -> k.partitionValue(id))));
    }

    public User updateUser(User user) {
        return userTable.updateItem(user);
    }

    public void deleteUser(String id) {
        userTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }

    public List<User> getAllUsers() {
        // Scan is expensive, usually not recommended for large tables
        return userTable.scan().items().stream().collect(Collectors.toList());
    }
}
