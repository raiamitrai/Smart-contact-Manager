package com.amit.SmartcontactManager.controller;

import com.amit.SmartcontactManager.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title" , "Home - Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title" , "About - Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title" , "Signup - Smart Contact Manager");
        model.addAttribute("user" , new User());
        return "signUp";
    }

    // this hander for registering user
    @PostMapping("/do_register")
    public String registeruser(@ModelAttribute("user") User user  , @RequestParam(value = "agreement" , defaultValue = "false") boolean agreement , Model model){
        System.out.println("Agreement" + agreement);
        System.out.println("user" + user);
        return "signUp";
    }

}
