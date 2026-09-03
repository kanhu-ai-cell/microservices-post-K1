package com.microservice.post.controller;

import com.microservice.post.entity.Post;
import com.microservice.post.payload.PostDto;
import com.microservice.post.service.PostService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {


    @Autowired
    private PostService service;


    @PostMapping("/save")
    public ResponseEntity<Post> savePost(@RequestBody Post post){
        Post savedPost=service.savePost(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public Post getPostByPostId(@PathVariable String postId){
        Post post=service.findByPostId(postId);
        return post;
    }

    @GetMapping("/{postId}/commentlist")
    @CircuitBreaker(name = "commentService", fallbackMethod = "commentFallback")
    public ResponseEntity<PostDto> getPostWithComments(@PathVariable String postId){
              PostDto postDto=service.getPostWithComments(postId);
        return new ResponseEntity<>(postDto,HttpStatus.OK);
    }

    public ResponseEntity<PostDto> commentFallback(@PathVariable String postId,Exception ex){
        System.out.println("Fallback is executed because service is down"+ex.getMessage());
        ex.printStackTrace();
        PostDto dto=new PostDto();
        dto.setId("123");
        dto.setDescp("Service Down");
        dto.setContent("Service Down");
        dto.setTitle("Service Down");

        return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }
}
