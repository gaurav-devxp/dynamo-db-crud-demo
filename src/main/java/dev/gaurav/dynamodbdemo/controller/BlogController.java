package dev.gaurav.dynamodbdemo.controller;

import dev.gaurav.dynamodbdemo.dto.PostResponse;
import dev.gaurav.dynamodbdemo.service.BlogService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    private final BlogService service;

    public BlogController(BlogService service) {
        this.service = service;
    }

    // --- 1. Create Post (Expects JSON Body) ---
    @PostMapping("/posts")
    public String createPost(@RequestBody CreatePostRequest request) {
        // We pass the JSON fields to our service
        service.createPost(request.id, request.title, request.content, request.author);
        return "Post created with ID: " + request.id;
    }

    // --- 2. Add Comment (Expects JSON Body) ---
    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable String postId, @RequestBody CreateCommentRequest request) {
        service.addComment(postId, request.commentId, request.text, request.author);
        return "Comment added to Post: " + postId;
    }

    // --- DTOs (Helper classes to map the JSON) ---
    public static class CreatePostRequest {
        public String id;
        public String title;
        public String content;
        public String author;
    }

    public static class CreateCommentRequest {
        public String commentId;
        public String text;
        public String author;
    }

    // (Keep the GET method the same)
    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable String postId) {
        return service.getFullPost(postId);
    }
}
