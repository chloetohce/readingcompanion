package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.service.UserService;

@Controller
@RequestMapping("")
public class LoginController {

    @Autowired
    UserService userService;
    
    @GetMapping("")
    public String landingPage() {
        return "landing";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    // @PostMapping("/login") // TODO: This method is not needed
    // public String login(@ModelAttribute User user) {
    //     System.out.println(user.getUsername());
    //     System.out.println(user.getPassword());
    //     return "landing";
    // }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        /*
         * TODO: Add validation.
         * Can do manual searching here where userService.findUser() and if there is an account 
         * with the same username, can binding.rejectValue("username", ...)
         */

        userService.registerUser(user);
        return "redirect:/";

    }
    
}
