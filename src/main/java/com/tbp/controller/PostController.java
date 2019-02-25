package com.tbp.controller;

import com.tbp.model.Comment;
import com.tbp.model.Post;
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

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createPage(Model model) {
        model.addAttribute("myPost", new Post());
        model.addAttribute("message", "Blogging ...");
        return "createPost";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createPost(@ModelAttribute Post myPost, Model model) {
        if(!StringUtils.hasText(myPost.getText()) || !StringUtils.hasText(myPost.getTitle())
        || !StringUtils.hasText(myPost.getUsername())) {
            model.addAttribute("myPost", myPost);
            model.addAttribute("message", "Don't forget to fill everything!");
            return "createPost";
        }
        postRepository.save(myPost);
        return "redirect:list";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String listPage(Model model) {
        Iterable<Post> all = postRepository.findAll();
        model.addAttribute("posts", all);
        return "listPost";
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.GET)
    public String readPage(Model model, @PathVariable Long id) {
        Post post = postRepository.findOne(id);
        List<Comment> commentList = commentRepository.findByPost(post);
        model.addAttribute("myPost", post);
        model.addAttribute("commentList", commentList);
        return "readPost";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete( @PathVariable Long id) {
        Post post = postRepository.findOne(id);
        List<Comment> commentList = commentRepository.findByPost(post);
        for(Comment comment : commentList) {
            commentRepository.delete(comment);
        }
        postRepository.delete(id);
        return "redirect:/post/list";
    }





}
