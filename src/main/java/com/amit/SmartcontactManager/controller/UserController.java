package com.amit.SmartcontactManager.controller;

import com.amit.SmartcontactManager.entities.Contact;
import org.springframework.ui.Model;
import com.amit.SmartcontactManager.entities.User;
import com.amit.SmartcontactManager.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ye method har request pe chalega
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        if (principal != null) {
            String userName = principal.getName();
            User user = userRepository.getUserByUserName(userName);
            model.addAttribute("user", user);
        }
    }

    @RequestMapping("/index")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, Principal principal) {
        // 1. Current logged-in user nikalna
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);

        // 2. Contact ko us user ke saath link karna
        contact.setUser(user);
        user.getContacts().add(contact);

        // 3. User save karne se contact bhi save ho jayega
        userRepository.save(user);

        return "normal/add_contact_form";
    }

}

