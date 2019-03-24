package com.tbp.controller;


import com.tbp.interceptor.UserSession;
import com.tbp.model.User;
import com.tbp.repository.UserRepository;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserSession userSession;

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createPage(Model model) {
        model.addAttribute("myUser", new User());
        model.addAttribute("message", "Create your account");
        return "userCreate";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@ModelAttribute User myUser, Model model) {
        User user = userRepository.findByUsername(myUser.getUsername());
        if(user == null) {
            // criptografando password
            myUser.setPassword(encryptPass(myUser.getPassword()));
            userRepository.save(myUser);
            return "redirect:/post/list";
        } else {
            model.addAttribute("myUser", myUser);
            model.addAttribute("message", "Choose another username!");
        }
        return "userCreate";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        model.addAttribute("myUser", new User());
        model.addAttribute("message", "Sign in");
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@ModelAttribute User myUser, Model model) {
        User user = userRepository.findByUsername(myUser.getUsername());
        if(user != null && checkPass(myUser.getPassword(), user.getPassword())) {
            userSession.addLoggerUser(user);
            return "redirect:/post/list";
        } else {
            model.addAttribute("myUser", new User());
            model.addAttribute("message", "Something went wrong! Try again.");
        }
        return "login";
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/post/list";
    }

    String encryptPass(String password) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String s = passwordEncryptor.encryptPassword(password);
        return s;
    }

    boolean checkPass(String plainPass, String encryptPass) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        return passwordEncryptor.checkPassword(plainPass, encryptPass);

    }
}
