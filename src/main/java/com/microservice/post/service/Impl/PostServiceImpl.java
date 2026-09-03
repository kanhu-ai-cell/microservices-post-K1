package com.microservice.post.service.Impl;

import com.microservice.post.config.RestTemplateConfig;
import com.microservice.post.entity.Post;
import com.microservice.post.payload.CommentDto;
import com.microservice.post.payload.PostDto;
import com.microservice.post.repository.PostRepository;
import com.microservice.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RestTemplateConfig restTemplate;

    @Override
    public Post savePost(Post post) {
        String postid = UUID.randomUUID().toString();
        post.setId(postid);
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    @Override
    public Post findByPostId(String postId) {
        Post post = postRepository.findById(postId).get();
        return post;
    }

    @Override
    public PostDto getPostWithComments(String postId) {
        Post post = postRepository.findById(postId).get();

        ArrayList comments = restTemplate.getRestTemplate().getForObject("http://COMMENT-SERVICE/api/comment/list/" + postId, ArrayList.class);
       if(comments !=null) {
           PostDto postDto = new PostDto();
           postDto.setId(post.getId());
           postDto.setTitle(post.getTitle());
           postDto.setDescp(post.getDescp());
           postDto.setContent(post.getContent());
           postDto.setCommentList(comments);
           return postDto;
       }else{
           PostDto postDto = new PostDto();
           postDto.setId(post.getId());
           postDto.setTitle(post.getTitle());
           postDto.setDescp(post.getDescp());
           postDto.setContent(post.getContent());
           return postDto;
       }
    }
}
