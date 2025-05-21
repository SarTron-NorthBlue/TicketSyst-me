package com.example.ticketapp.controller;

import com.example.ticketapp.model.User;
import com.example.ticketapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            // Générer un slug à partir du nom de l'entreprise
            String slug = user.getCompanyName().toLowerCase()
                            .replaceAll("[^a-z0-9]", "-")
                            .replaceAll("-+", "-");
            user.setCompanySlug(slug);
            
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "Compte créé avec succès !");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
