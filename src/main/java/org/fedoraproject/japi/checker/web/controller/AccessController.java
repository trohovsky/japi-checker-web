package org.fedoraproject.japi.checker.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccessController {

    @RequestMapping(value = "/login")
    public String login(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("message", message);
        return "access/login";
    }

    @RequestMapping(value = "/denied")
    public String denied() {
        return "access/denied";
    }

    @RequestMapping(value = "/login/failure")
    public String loginFailure() {
        String message = "Login Failure!";
        return "redirect:/login?message=" + message;
    }

    @RequestMapping(value = "/logout/success")
    public String logoutSuccess() {
        String message = "Logout Success!";
        return "redirect:/login?message=" + message;
    }

}
