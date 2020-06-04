package com.aptech.project.project_sem4.controller;

import com.aptech.project.project_sem4.model.User;
import com.aptech.project.project_sem4.service.AdminService;
import com.aptech.project.project_sem4.service.QuizService;
import com.aptech.project.project_sem4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    public User getUser(HttpServletRequest request)
    {
        String user_id = request.getParameter("user_id");
        User user = adminService.findUserbyId(user_id);
        return user;
    }
    @RequestMapping(value = "/changeProfile",method = RequestMethod.POST)
    public User updateUser(HttpServletRequest request)
    {
        String userEmail = request.getParameter("userEmail");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        User user = userService.findByEmail(userEmail);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        userService.saveUserProfile(user);
        return user;
    }
    @RequestMapping(value = "/DeleteUser", method = RequestMethod.POST)
    public String DeleteUser(HttpServletRequest request)
    {
        String user_id = request.getParameter("user_id");
        userService.Delete_User(user_id);
        return "Xong";
    }

    @RequestMapping(value = "/AddNewAdmin", method = RequestMethod.POST)
    public String AddnewAdmin(HttpServletRequest request, ModelAndView modelAndView, @RequestParam Map<String, String> requestParams)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String userEmail = request.getParameter("userEmail");
        String userPass = request.getParameter("userPass");
//        User userExits = userService.findByEmail(userEmail);
        String pass = bCryptPasswordEncoder.encode(userPass);
        System.out.println(pass);
        User new_Admin = new User();
        new_Admin.setConfirmationToken(UUID.randomUUID().toString());
        new_Admin.setFirstName(firstName);
        new_Admin.setLastName(lastName);
        new_Admin.setPassword(pass);
        new_Admin.setEmail(userEmail);
        adminService.saveUser(new_Admin);
//        String pass = bCryptPasswordEncoder.encode(request.getParameter("userPass"));

        return "Yes";
    }
}
