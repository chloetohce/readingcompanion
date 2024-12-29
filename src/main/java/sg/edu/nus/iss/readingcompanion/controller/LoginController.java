package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.service.UserService;

@Controller
@RequestMapping("")
public class LoginController {

    @Autowired
    UserService userService;
    
    @GetMapping("")
    public String landingPage() {
        return "redirect:/books";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult binding) {
        try {
            userService.registerUser(user);
        } catch (Exception e) {
            FieldError err = new FieldError("user", "username", e.getMessage());
            binding.addError(err);
            e.printStackTrace();
        }

        if (binding.hasErrors()) {
            return "register";
        }

        return "redirect:/";

    }
    
}
