package com.amit.SmartcontactManager.controller;

import com.amit.SmartcontactManager.entities.User;
import com.amit.SmartcontactManager.help.Message;
import com.amit.SmartcontactManager.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // Home page
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    // About page
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    // Signup page
    @GetMapping("/signup")
    public String signup(Model model, HttpSession session) {
        model.addAttribute("title", "Signup - Smart Contact Manager");
        model.addAttribute("user", new User());

        // Load and remove flash message (if any)
        Message message = (Message) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }

        return "signUp"; // Return signUp.html
    }

    // Handle user registration
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, BindingResult result, HttpSession session) {

        try {
            if (!agreement) {
                throw new Exception("You have not agreed to the terms and conditions.");
            }

            if(result.hasErrors()){
                model.addAttribute("user",user);
                return "signUp";
            }

            // Set user defaults
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageName("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Save user to database
            userRepository.save(user);

            // Show success message
            session.setAttribute("message",
                    new Message("Registered successfully !!", "alert-success"));

            return "redirect:/signup";

        } catch (Exception e) {
            e.printStackTrace();

            // Reset form and show error
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));

            return "redirect:/signup";
        }
    }
    // handler for login

    @GetMapping("/signin")
    public String customLogin(){
        return "login";
    }
}
