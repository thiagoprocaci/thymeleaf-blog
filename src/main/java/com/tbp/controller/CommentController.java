package com.tbp.controller;

import com.tbp.model.Comment;
import com.tbp.model.Post;
import com.tbp.repository.CommentRepository;
import com.tbp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("comment")
public class CommentController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createComment(@RequestParam("username") String username,
                                @RequestParam("text") String text,
                                @RequestParam("postId") Long postId) {

        if(StringUtils.hasText(text) && StringUtils.hasText(username)) {
            Post post = postRepository.findOne(postId);
            Comment comment = new Comment();
            comment.setUsername(username);
            comment.setText(text);
            comment.setPost(post);
            commentRepository.save(comment);
        }
        return "redirect:/post/read/" + postId;

    }

}
