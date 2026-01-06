package dev.gaurav.dynamodbdemo.dto;

import java.util.List;

public class PostResponse {
    public String postId;
    public String title;
    public String content;
    public List<CommentDto> comments;
}
