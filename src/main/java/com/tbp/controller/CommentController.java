package com.tbp.controller;

import com.tbp.interceptor.UserSession;
import com.tbp.model.Comment;
import com.tbp.model.Post;
import com.tbp.model.User;
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
    @Autowired
    UserSession userSession;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createComment(@RequestParam("text") String text,
                                @RequestParam("postId") Long postId) {

        if(StringUtils.hasText(text)) {
            Post post = postRepository.findOne(postId);
            Comment comment = new Comment();
            comment.setText(text);
            comment.setPost(post);
            User loggerUser = userSession.getLoggerUser();
            comment.setUser(loggerUser);
            commentRepository.save(comment);
        }
        return "redirect:/post/read/" + postId;

    }

}
