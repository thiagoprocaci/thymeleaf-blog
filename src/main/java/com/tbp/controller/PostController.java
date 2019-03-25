package com.tbp.controller;

import com.tbp.interceptor.UserSession;
import com.tbp.model.Comment;
import com.tbp.model.Post;
import com.tbp.model.User;
import com.tbp.repository.CommentRepository;
import com.tbp.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("post")
public class PostController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserSession userSession;






}
