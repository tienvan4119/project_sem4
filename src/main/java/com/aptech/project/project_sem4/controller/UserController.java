package com.aptech.project.project_sem4.controller;

import com.aptech.project.project_sem4.model.Role;
import com.aptech.project.project_sem4.model.Section;
import com.aptech.project.project_sem4.model.Topic;
import com.aptech.project.project_sem4.model.User;
import com.aptech.project.project_sem4.repository.RoleRepository;
import com.aptech.project.project_sem4.repository.UserRepository;
import com.aptech.project.project_sem4.service.EmailService;
import com.aptech.project.project_sem4.service.QuizService;
import com.aptech.project.project_sem4.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuizService quizService;
    @RequestMapping(value = {"/", "/section"}, method = RequestMethod.GET)
    public String section(Model model)
    {
        List<User> listUser = userService.listAll();
        List <Section> listSection = userService.listAllSection();
        model.addAttribute("listUser", listUser);
        model.addAttribute("listSection", listSection);

        return "section";
    }
    @RequestMapping(value = {"/createQuiz"})
    public String createQuiz(Model model, HttpServletRequest request)
    {
        String params = request.getQueryString();
        String [] section_id = params.split("=");
        System.out.println(section_id[1]);
        List<Topic> listTopic = quizService.listAllTopicBySection_id(section_id[1]);
        model.addAttribute("listTopic", listTopic);
        System.out.println(section_id[1]);

        return "quiz";
    }


}
