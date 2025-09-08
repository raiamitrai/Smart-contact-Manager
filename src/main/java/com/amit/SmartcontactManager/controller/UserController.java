package com.amit.SmartcontactManager.controller;

import com.amit.SmartcontactManager.entities.Contact;
import com.amit.SmartcontactManager.repo.ContactRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import com.amit.SmartcontactManager.entities.User;
import com.amit.SmartcontactManager.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

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
    public String processContact(
            @ModelAttribute Contact contact, Model model,
            @RequestParam("profileImage")
            MultipartFile file, Principal principal) {

        try{
            // 1. Current logged-in user nikalna
            String userName = principal.getName();
            User user = userRepository.getUserByUserName(userName);

            //processing and uploading file
            if(file.isEmpty()){
                // if the file is empty the try oue message;
                System.out.println("File is empty");
                contact.setImage("contact.png");
            }
            else{
                //upload file
                contact.setImage(file.getOriginalFilename());

                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());

                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("file is uploded");
            }

            // 2. Contact ko us user ke saath link karna
            contact.setUser(user);
            user.getContacts().add(contact);

            // 3. User save karne se contact bhi save ho jayega
            userRepository.save(user);

            model.addAttribute("message", "✅ Contact added successfully!");
            model.addAttribute("alertType", "success");
        }
        catch (Exception e){
            e.printStackTrace();

            // Error case
            model.addAttribute("message", "❌ Something went wrong!");
            model.addAttribute("alertType", "danger");
        }
        return "normal/add_contact_form";
    }

    // view contact handler
    @GetMapping("/contacts")
    public String viewContact(@RequestParam(defaultValue = "0") int page, Principal principal , Model m){
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        Pageable pageable = PageRequest.of(page, 5); // 5 contacts per page

        Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId() , pageable);
        m.addAttribute("contacts" , contacts);

        return "normal/view_contacts";
    }
    // delete contact
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cid, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            Contact contact = contactRepository.findById(cid).orElseThrow(() -> new Exception("Contact not found"));

            // Ensure contact belongs to the logged-in user
            String userName = principal.getName();
            User user = userRepository.getUserByUserName(userName);

            if (contact.getUser().getId() == (user.getId())) {
                contactRepository.delete(contact);
                redirectAttributes.addFlashAttribute("message", "✅ Contact deleted successfully!");
                redirectAttributes.addFlashAttribute("alertType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "❌ You are not authorized to delete this contact.");
                redirectAttributes.addFlashAttribute("alertType", "danger");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "❌ Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "danger");
        }
        return "redirect:/user/contacts";
    }





}

