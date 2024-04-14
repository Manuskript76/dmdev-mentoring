package com.vmdev.eshop.http.controller;

import com.vmdev.eshop.dto.ClientCreateEditDto;
import com.vmdev.eshop.entity.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "client/login";
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("client") ClientCreateEditDto clientDto) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("client", clientDto);
        return "client/registration";
    }
}
