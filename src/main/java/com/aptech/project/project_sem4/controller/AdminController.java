package com.aptech.project.project_sem4.controller;

import com.aptech.project.project_sem4.model.*;
import com.aptech.project.project_sem4.service.AdminService;
import com.aptech.project.project_sem4.service.QuizService;
import com.aptech.project.project_sem4.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private QuizService quizService;

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
        new_Admin.setFirstName(firstName);
        new_Admin.setLastName(lastName);
        new_Admin.setPassword(pass);
        new_Admin.setEmail(userEmail);
        adminService.saveAdmin(new_Admin);
//        String pass = bCryptPasswordEncoder.encode(request.getParameter("userPass"));

        return "Yes";
    }

    @RequestMapping(value = "/AddNewTeacher", method = RequestMethod.POST)
    public String AddNewTeacher(HttpServletRequest request, ModelAndView modelAndView, @RequestParam Map<String, String> requestParams)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String userEmail = request.getParameter("userEmail");
        String userPass = request.getParameter("userPass");
        String classID = request.getParameter("userClassID");
        ObjectId classID_convert = new ObjectId(classID);
//        User userExits = userService.findByEmail(userEmail);
        String pass = bCryptPasswordEncoder.encode(userPass);
        System.out.println(pass);
        User new_Admin = new User();
        new_Admin.setFirstName(firstName);
        new_Admin.setLastName(lastName);
        new_Admin.setPassword(pass);
        new_Admin.setEmail(userEmail);
        new_Admin.setClassID(classID_convert);
        adminService.saveAdmin(new_Admin);
//        String pass = bCryptPasswordEncoder.encode(request.getParameter("userPass"));

        return "Yes";
    }


    @RequestMapping(value = "/AddNewUser", method = RequestMethod.POST)

    public String AddnewUser(HttpServletRequest request, ModelAndView modelAndView, @RequestParam Map<String, String> requestParams)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String userEmail = request.getParameter("userEmail");
        String userPass = request.getParameter("userPass");
//       User userExits = userService.findByEmail(userEmail);
        String pass = bCryptPasswordEncoder.encode(userPass);
        System.out.println(pass);
        User new_user = new User();

        new_user.setFirstName(firstName);
        new_user.setLastName(lastName);
        new_user.setPassword(pass);
        new_user.setEmail(userEmail);
        adminService.saveUser(new_user);
//        String pass = bCryptPasswordEncoder.encode(request.getParameter("userPass"));
        return "Yes";
    }

    @RequestMapping(value= "/getTopic",  method = RequestMethod.POST)
    public List<Topic> display_Topic(Model model, HttpServletRequest request)
    {
        String section_id = request.getParameter("section_id");
        List<Topic> listTopic = quizService.listAllTopicBySection_id(section_id);
        //model.addAttribute("listTopic", listTopic);
        return listTopic;
    }
    @RequestMapping(value= "/getListSubject",  method = RequestMethod.POST)
    public List<Section> getListSubject(Model model, HttpServletRequest request)
    {
        String faculty_id = request.getParameter("faculty_id");
        List<Section> listSection = adminService.getListSubject(faculty_id);
        return listSection;

    }

    @RequestMapping(value = "/addQuestion", method = RequestMethod.POST)
    public void addQuestion(Model model, HttpServletRequest request)
    {
        String question_desc = request.getParameter("question_desc");
        String choice1 = request.getParameter("choice1");
        String choice2 = request.getParameter("choice2");
        String choice3 = request.getParameter("choice3");
        String choice4 = request.getParameter("choice4");
        String answer = request.getParameter("answer");
        String section_id = request.getParameter("section_id");
        String topic_title = request.getParameter("topic_title");
        Question newQuestion = create_Question(question_desc, topic_title, section_id);
        Choice Choice_1 = new Choice();
        Choice Choice_2 = new Choice();
        Choice Choice_3 = new Choice();
        Choice Choice_4 = new Choice();

        Choice_1.setId(new ObjectId());
        Choice_2.setId(new ObjectId());
        Choice_3.setId(new ObjectId());
        Choice_4.setId(new ObjectId());

        Choice_1.setChoice_desc(choice1);
        Choice_2.setChoice_desc(choice2);
        Choice_3.setChoice_desc(choice3);
        Choice_4.setChoice_desc(choice4);

        Choice_1.setQuestion_id(newQuestion.getId());
        Choice_2.setQuestion_id(newQuestion.getId());
        Choice_3.setQuestion_id(newQuestion.getId());
        Choice_4.setQuestion_id(newQuestion.getId());



        adminService.saveQuestion(newQuestion);
        adminService.saveChoice(Choice_1);
        adminService.saveChoice(Choice_2);
        adminService.saveChoice(Choice_3);
        adminService.saveChoice(Choice_4);

        Answer newAnswer = new Answer();
        newAnswer.setId(new ObjectId());
        newAnswer.setQuestion_id(newQuestion.getId());
        newAnswer.setAnswer_id(adminService.findChoiceByChoiceDesc(answer).getId());
        adminService.saveAnswer(newAnswer);
    }

    public Question create_Question(String question_desc, String topic_title, String section_id)
    {
        Question newQuestion = new Question();
        newQuestion.setId(new ObjectId());
        newQuestion.setQuestion_desc(question_desc);
        newQuestion.setTopic_id(adminService.getTopicByTitleAndSectionId(topic_title, section_id).getId());
        return newQuestion;
    }


}
