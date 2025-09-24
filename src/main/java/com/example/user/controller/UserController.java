package com.example.user.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Exception.DataNotFoundException;
import com.example.user.model.CustomUserDetails;
import com.example.user.model.dto.UserCreateForm;
import com.example.user.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(), 
                    userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getCustomerId());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
    
    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
    	return "forgot_password";
    }
    
    @GetMapping("/change-password")
    public String changePasswordForm() {
    	return "change_password";
    }
    
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
    	try {
            String message = userService.resetPassword(email);
            model.addAttribute("message", message);
        } catch (DataNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (MessagingException e) {
			e.printStackTrace();
		}
        return "forgot_password";
    }
    
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                @AuthenticationPrincipal CustomUserDetails userDetails, 
                                Model model) {
        try {
            String message = userService.changePassword(userDetails.getUsername(), currentPassword, newPassword, confirmPassword);
            model.addAttribute("message", message);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "change_password";
    }
}