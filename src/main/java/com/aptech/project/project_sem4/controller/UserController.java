package com.aptech.project.project_sem4.controller;

import com.aptech.project.project_sem4.model.*;
import com.aptech.project.project_sem4.service.QuizService;
import com.aptech.project.project_sem4.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuizService quizService;
    private Random random = new Random();
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
       // model.addAttribute("listTopic", listTopic);
        System.out.println(section_id[1]);
        List<Question> listQuestion = quizService.listAllQuestion();
        UserController obj = new UserController();
        List<Question> newListQuestion = new ArrayList<Question>();
        for(int i = 0; i < 10; i++){
            Question a = getRandomList(listQuestion);
            newListQuestion.add(a);
            listQuestion.remove(a);
        }
        model.addAttribute("listQuestion", newListQuestion);
        List<String> listQuestion_id = new ArrayList<String>();
        List<Choice> listChoice = new ArrayList<Choice>();
        for(int i = 0; i< 10;i++)
        {
            String question_id = newListQuestion.get(i).getId().toString();
            listChoice.addAll(quizService.listChoiceByQuestion_id(question_id));
            System.out.println(question_id);
        }
        model.addAttribute("listChoice", listChoice);
        return "quiz";
    }

    public Question getRandomList(List<Question> list) {
        //0-4
        int index = random.nextInt(list.size());
        System.out.println("\nIndex :" + index );
        return list.get(index);
    }
}
