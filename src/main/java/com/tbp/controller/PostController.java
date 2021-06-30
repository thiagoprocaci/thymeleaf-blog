package com.tbp.controller;

import com.tbp.interceptor.UserSession;
import com.tbp.model.Comment;
import com.tbp.model.Post;
import com.tbp.model.PostLike;
import com.tbp.model.User;
import com.tbp.repository.CommentRepository;
import com.tbp.repository.PostLikeRepository;
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
    @Autowired
    PostLikeRepository postLikeRepository;


    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createPost(Model model) {
        model.addAttribute("myPost", new Post());
        model.addAttribute("message", "Blogging ....");
        return "createPost";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createPost(@ModelAttribute Post myPost, Model model) {
        if(!StringUtils.hasText(myPost.getText()) || !StringUtils.hasText(myPost.getTitle())) {
            model.addAttribute("myPost", myPost);
            model.addAttribute("messagem", "Dont forget the text and title");
            return "createPost";
        }
        User loggedUser = userSession.getLoggerUser();
        myPost.setUser(loggedUser);
        postRepository.save(myPost);
        return "redirect:list";
    }


    @RequestMapping(value = "list", method = RequestMethod.GET)
   public String listPage(Model model,
                          @RequestParam(value = "searchString", required = false) String searchString) {
        Iterable<Post> posts;
        if(StringUtils.hasText(searchString)) {
            posts = postRepository.findByText(searchString);
        } else {
            posts = postRepository.findAll();
        }
        model.addAttribute("posts", posts);
        return "listPost";
   }

   @RequestMapping(value = "read/{id}", method = RequestMethod.GET)
   public String read(Model model, @PathVariable Long id) {
        Post post = postRepository.findOne(id);
        List<Comment> commentList = commentRepository.findByPost(post);
        model.addAttribute("myPost", post);
        model.addAttribute("commentList", commentList);

       Long totalLikes = postLikeRepository.countByPost(post);
       model.addAttribute("totalLikes", totalLikes);

       return "readPost";
   }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable Long id) {
        Post post = postRepository.findOne(id);
        List<Comment> commentList = commentRepository.findByPost(post);
        for(Comment comment : commentList) {
            commentRepository.delete(comment);
        }
        List<PostLike> postLikeList = postLikeRepository.findByPost(post);
        for(PostLike postLike : postLikeList) {
            postLikeRepository.delete(postLike);
        }
        postRepository.delete(post);
        return "redirect:/post/list";
    }

    @RequestMapping(value = "like/{id}", method = RequestMethod.GET)
    public String like(@PathVariable Long id) {

        Post post = postRepository.findOne(id);
        User loggedUser = userSession.getLoggerUser();

        // fazendo a verificacao se existe like...
        PostLike postLike = postLikeRepository.findByPostAndUser(post, loggedUser);
        if(postLike == null) {
            // nao dei like ainda
            postLike = new PostLike();
            postLike.setPost(post);
            postLike.setUser(loggedUser);
            postLikeRepository.save(postLike);
        } else {
            postLikeRepository.delete(postLike);
        }
        return "redirect:/post/read/" + id;
    }

}
