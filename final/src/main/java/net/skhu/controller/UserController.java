package net.skhu.controller;

import jakarta.servlet.http.HttpSession;
import net.skhu.entity.UserEntity;
import net.skhu.model.UserEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import net.skhu.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userEdit", new UserEdit());
        return "signup";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "index";
    }

    @GetMapping("/account")
    public String accountForm(HttpSession session, Model model) {
        String userid = (String) session.getAttribute("userId");
        UserEntity userEntity = userService.findByUserid(userid);
        if (userEntity != null) {
            model.addAttribute("user", userEntity);
            return "account";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updateNickname")
    public String updateNickname(@RequestParam String newNickname, HttpSession session) {
        String userid = (String) session.getAttribute("userId");
        userService.updateNickname(userid, newNickname);
        return "redirect:/account";
    }


    @GetMapping("/changePw")
    public String changePwForm() {
        return "changePw";
    }


    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute UserEdit userEdit) {
        UserEntity userEntity = convertToEntity(userEdit);
        userService.createUser(userEntity);
        return "redirect:/welcome";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userid, @RequestParam String password,
        HttpSession session) {
        boolean authenticated = userService.authenticate(userid, password);
        if (authenticated) {
            session.setAttribute("userId", userid);
            return "redirect:/mainhome";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String newPassword,
        @RequestParam String confirmPassword,
        HttpSession session) {
        String userid = (String) session.getAttribute("userId");
        if (userid != null && newPassword.equals(confirmPassword)) {
            userService.changePassword(userid, newPassword);
            return "redirect:/account";
        } else {
            return "redirect:/changePw";
        }
    }

    private UserEntity convertToEntity(UserEdit userEdit) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserid(userEdit.getUserid());
        userEntity.setPw(userEdit.getPw());
        userEntity.setUsername(userEdit.getUsername());
        userEntity.setNickname(userEdit.getNickname());
        return userEntity;
    }



}
