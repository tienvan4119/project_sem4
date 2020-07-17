package com.aptech.project.project_sem4.controller;

import com.aptech.project.project_sem4.model.*;
import com.aptech.project.project_sem4.model.Class;
import com.aptech.project.project_sem4.repository.RoleRepository;
import com.aptech.project.project_sem4.repository.SectionRepository;
import com.aptech.project.project_sem4.repository.UserRepository;
import com.aptech.project.project_sem4.service.AdminService;
import com.aptech.project.project_sem4.service.EmailService;
import com.aptech.project.project_sem4.service.UserService;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class RegisterController {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    public RegisterController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService, EmailService emailService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.emailService = emailService;

    }

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login( ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    // Return registration form template
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    //Return registration form template
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        User userExits = userService.findByEmail(user.getEmail());
        System.out.println(userExits);
        if (userExits != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            modelAndView.setViewName("register");
            bindingResult.reject("email");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        } else {

            user.setPassword("abc");


            userService.saveUser(user);
            String appUrl = request.getScheme() + "://" + request.getServerName();


            modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());
            modelAndView.setViewName("register");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", user);
        modelAndView.addObject("fullName", "Welcome " + user.getFirstName() + " " + user.getLastName());
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("dashboard");
        return modelAndView;
    }

    @RequestMapping(value = {"/","/home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin"})
    public String UserView(Model model)
    {
        List<Faculty> listFaculty = adminService.getAllFaculty();
        model.addAttribute("listFaculty", listFaculty);
        return "admin";
    }

    @RequestMapping(value = {"/index2"})
    public String listUser(Model model)
    {
        List <User> list_AllUser = userService.listAll();
        List <Role> listRole = userService.listAllRole();
        List<User> listUser = new ArrayList<User>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("USER"))
            {
                listUser.add(list_AllUser.get(i));
            }
        }
        model.addAttribute("listUser", listUser);
        model.addAttribute("userRole", listRole);
        return "index2";
    }

    @RequestMapping(value = {"/form"})
    public String addQuestion(Model model)
    {
        List<Section> listSection = adminService.getAllSection();
        model.addAttribute("listSection", listSection);

        return "form";
    }

    @RequestMapping(value = {"/khoa"})
    public String Khoa_Page(Model model)
    {
        List<Faculty> listFaculty = adminService.getAllFaculty();
        model.addAttribute("listFaculty", listFaculty);

        return "khoa";
    }

    @RequestMapping(value = {"/form2"})
    public String updateQuestion(Model model)
    {
        List<Faculty> listFaculty = adminService.getAllFaculty();

        model.addAttribute("listFaculty", listFaculty);
        return "form2";
    }

    @RequestMapping(value = {"/teacher"})
    public String teacherPage()
    {
        return "teacher";
    }

    @RequestMapping(value = {"/addQuiz"})
    public String addQuizPage()
    {
        return "addQuiz";
    }

    @RequestMapping(value = {"/updateQuiz"})
    public String updateQuizQuizPage()
    {
        return "updateQuiz";
    }

    @RequestMapping(value = {"/class"})
    public String classPage(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Course> listCourseOfTeacher = adminService.getListCourseOfTeacher(user.getId().toString());
        model.addAttribute("listCourse", listCourseOfTeacher);
        return "class";
    }
    @RequestMapping(value = {"/userProfile"})
    public String userProfile(Model model)  
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User current_user = userService.findByEmail(userName);
        Class user_class = userService.findClassByID(current_user.getClassID().toString());
        model.addAttribute("current_user", current_user);
        model.addAttribute("user_class", user_class);
        return "userProfile";
    }
}

