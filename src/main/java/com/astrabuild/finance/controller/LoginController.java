package com.astrabuild.finance.controller;

import com.astrabuild.finance.entity.User;
import com.astrabuild.finance.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password) && user.getRole().equals("ACCOUNTANT")) {
            session.setAttribute("loggedInUser", user);  // store user in session
            model.addAttribute("username", username);
            return "accountant-dashboard";
        } else {
            model.addAttribute("error", "Invalid credentials or role!");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // clear the session
        return "redirect:/login";
    }

}
