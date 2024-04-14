/**
 * Controller class for 'User'
 */
package com.library.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Annotate as controller
@Controller
public class UserController {

    // Inject instance of UserService
    @Autowired
    private UserService service;

    // Map HTTP GET requests for '/register'
    @GetMapping("/register")
    public String showNewForm(Model model) {
        // Bind new user to attribute
        model.addAttribute("user", new User());

        // Return templates/register.html
        return "register";
    }

    // Map HTTP POST requests for '/register/success'
    // Process user registrations here
    @PostMapping("/register/success")
    public String saveUser(User user) {
        // Use BCrypt to encode user password...
        // and set it to the user's password, so...
        // that we may store encrypted user password in database
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Try-catch block because this operation might encounter an exception
        try {
            // Attempt to save user
            service.save(user);
            // Success, redirect to "/login?registered". Param registered...
            // indicates successful registration for status message
            return "redirect:/login?registered";
            // Exception found
            // Likely tried to register non-unique email address
        } catch (DataIntegrityViolationException e) {
            // Return to "/register?fail". Param fail...
            // indicates failure to register, so we can...
            // display appropriate status message
            return "redirect:/register?fail";
            // Exception found
            // Probably something else
        } catch (Exception e) {
            // Return to "/register?error". Param error...
            // indicates error in registering, so we can...
            // display appropriate status message
            return "redirect:/register?error";
        }


    }

    // Map HTTP GET requests for '/login'
    @GetMapping("/login")
    public String showLoginForm(Model model) {

        // Return templates/login.html
        return "login";
    }

    // Map HTTP GET requests for '/logout'
    @GetMapping("/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        // Find the auth (user) trying to logout
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Found.
        if (auth != null) {
            // Let Spring Security handle the logout with...
            // the config done in AuthConfig
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // Return redirect to login page with param logout for success message
        return "redirect:/login?logout";
    }
}
