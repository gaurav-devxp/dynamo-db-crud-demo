package dev.gaurav.dynamodbdemo.service;

import dev.gaurav.dynamodbdemo.dto.CommentDto;
import dev.gaurav.dynamodbdemo.dto.PostResponse;
import dev.gaurav.dynamodbdemo.model.Blog;
import dev.gaurav.dynamodbdemo.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogService {
    private final BlogRepository repository;

    public BlogService(BlogRepository repository) {
        this.repository = repository;
    }

    public void createPost(String postId, String title, String content, String author) {
        Blog post = new Blog();
        post.setPk("POST#" + postId);
        post.setSk("META"); // <--- Designates this row as the Parent
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        repository.save(post);
    }

    public void addComment(String postId, String commentId, String text, String author) {
        Blog comment = new Blog();
        comment.setPk("POST#" + postId); // Same PK as the post!
        comment.setSk("COMMENT#" + commentId); // <--- Unique SK for comment
        comment.setCommentText(text);
        comment.setAuthor(author);
        comment.setCreationDate(new Date().toString());
        repository.save(comment);
    }

    // THE "SINGLE TABLE" QUERY LOGIC
    public PostResponse getFullPost(String postId) {
        // 1. Fetch all rows (Post metadata + Comments) in one DB call
        List<Blog> items = repository.getPostWithComments(postId);

        if (items.isEmpty()) return null;

        PostResponse response = new PostResponse();
        response.comments = new ArrayList<>();

        // 2. Separate the "Head" (Post) from the "Tail" (Comments)
        for (Blog item : items) {
            if ("META".equals(item.getSk())) {
                // This is the Post Metadata
                response.postId = postId;
                response.title = item.getTitle();
                response.content = item.getContent();
            } else if (item.getSk().startsWith("COMMENT#")) {
                // This is a Comment
                CommentDto c = new CommentDto();
                c.commentId = item.getSk().replace("COMMENT#", "");
                c.text = item.getCommentText();
                c.author = item.getAuthor();
                response.comments.add(c);
            }
        }
        return response;
    }
}
