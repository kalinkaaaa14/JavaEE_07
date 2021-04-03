package com.kupchyk.javaee.controller;

import com.kupchyk.javaee.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.kupchyk.javaee.service.UserService;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-in")
    public String getSignIn(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("error", null);
        return "login";
    }

    @PostMapping("/sign-in")
    public String postSignIn(@ModelAttribute User user, Model model) {
        List<String> error = userService.login(user.getUsername(),user.getPassword());
        if(error.isEmpty()){
            return "redirect:/";
        }
        model.addAttribute("err", error.get(0));
        return "login";
    }

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/sign-up")
    public String postSignUp(@ModelAttribute User user, Model model) {
       List<String> errors = userService.signup(user);
        if (errors.isEmpty()) {
            return "redirect:/sign-in";
        }
        model.addAttribute("error", errors.get(0));
        return "signup";
    }

}
