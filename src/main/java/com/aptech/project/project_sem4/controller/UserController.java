package com.aptech.project.project_sem4.controller;

import com.aptech.project.project_sem4.model.*;
import com.aptech.project.project_sem4.model.Class;
import com.aptech.project.project_sem4.repository.UserRepository;
import com.aptech.project.project_sem4.service.AdminService;
import com.aptech.project.project_sem4.service.QuizService;
import com.aptech.project.project_sem4.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRepository userRepository;
    private Random random = new Random();

//    @RequestMapping(value = {"/", "/section"}, method = RequestMethod.GET)
//    public String section(Model model) {
//        List<User> listUser = userService.listAll();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//        User current_user = userService.findByEmail(userName);
//        Class user_class = userService.findClassByID(current_user.getClassId().toString());
//        List<RelationStudentCourse> listCourseofStudent = adminService.getListCourseofStudent(current_user.getId().toString());
//        List<Course> listCourse = new ArrayList<>();
//        for(int i = 0; i < listCourseofStudent.size();i++)
//        {
//            listCourse.add(adminService.findCoursebyID(listCourseofStudent.get(i).getCourseID().toString()));
//        }
//        List<Test> listTest = new ArrayList<>();
//        for (int i = 0; i<listCourse.size();i++)
//        {
//            List<Test> test = adminService.getListTestOfCourse(listCourse.get(i).getId().toString());
//            for(int j=0;j<test.size();j++)
//            {
//                listTest.add(test.get(j));
//            }
//        }
//        List<Course> listCourseOfTest = new ArrayList<>();
//        for(int i = 0; i< listTest.size(); i++)
//        {
//            listCourseOfTest.add( adminService.findCoursebyID(listTest.get(i).getCourseID().toString()));
//        }
//
//        model.addAttribute("current_user", current_user);
//        model.addAttribute("courseOfTest", listCourseOfTest);
//        model.addAttribute("listTest", listTest);
//        model.addAttribute("user_class", user_class);
//        model.addAttribute("listCourse", listCourse);
//        return "section";
//    }

    @RequestMapping (value = {"/getSection"})
    public String getListTestForSection(Model model, HttpServletRequest request)
    {
        String params = request.getQueryString();
        String[] course = params.split("=");
        String course_id = course[1];

        List<Test> listTest = adminService.getListTestOfCourse(course_id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User current_user = userService.findByEmail(userName);
        Class user_class = userService.findClassByID(current_user.getClassId().toString());
        List<RelationStudentCourse> listCourseofStudent = adminService.getListCourseofStudent(current_user.getId().toString());
        List<Course> listCourse = new ArrayList<>();
        Course currentCourse  = adminService.findCoursebyID(course_id);
        for(int i = 0; i < listCourseofStudent.size();i++)
        {
            listCourse.add(adminService.findCoursebyID(listCourseofStudent.get(i).getCourseID().toString()));
        }
        List<Result> listResult = userService.getListResultofStudent(course_id, current_user.getId().toString());

        model.addAttribute("listResult",listResult);
        model.addAttribute("listTest", listTest);
        model.addAttribute("currentCourse", currentCourse);
        model.addAttribute("current_user", current_user);
        model.addAttribute("user_class", user_class);
        model.addAttribute("listCourse", listCourse);
        return "section";
    }


    @RequestMapping(value = {"/userResult"})
    public String userResult(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User current_user = userService.findByEmail(userName);
        List<Result> listResult = userService.getListSection_Result(current_user.getId().toString());
        List<Section> listSection = userService.listAllSection();
        model.addAttribute("listResult", listResult);
        model.addAttribute("listSection", listSection);
        return "userResult";
    }

    @RequestMapping(value = {"/createQuiz"})
    public String createQuiz(Model model, HttpServletRequest request) {
        String params = request.getQueryString();
        String[] test_id = params.split("=");
        System.out.println(test_id[1]);
        Section current_section = quizService.isDone(userService.getCourseSection(adminService.getTestbyId(test_id[1]).getCourseID().toString()).getSectionId().toString());

        List<Topic> listTopic = quizService.listAllTopicBySection_id(current_section.getId().toString());
        // model.addAttribute("listTopic", listTopic);

        List<Question> listQuestion = quizService.listAllQuestion();
        List<Question> listQuestionyByTopic = new ArrayList<Question>();

        UserController obj = new UserController();
        List<Question> newListQuestion = new ArrayList<Question>();
        Test test = adminService.getTestbyId(test_id[1]);
        int numberOfQuestion = test.getNumberQuestion();
        for (int i = 0; i < listTopic.size(); i++) {
            listQuestionyByTopic.addAll(quizService.listQuestionByTopic(listTopic.get(i).getId().toString()));
        }

        for (int i = 0; i < numberOfQuestion; i++) {
            Question a = getRandomList(listQuestionyByTopic);
            newListQuestion.add(a);
            listQuestionyByTopic.remove(a);
        }
        model.addAttribute("listQuestion", newListQuestion);
        List<Choice> listChoice = new ArrayList<Choice>();
        for (int i = 0; i < numberOfQuestion; i++) {
            String question_id = newListQuestion.get(i).getId().toString();
            listChoice.addAll(quizService.listChoiceByQuestion_id(question_id));
            System.out.println(question_id);
        }
        for (int i = 0; i < numberOfQuestion; i++) {
            Session session_question = new Session();
            session_question.setQuestion_id(newListQuestion.get(i).getId());
            ObjectId choice_Pre = new ObjectId();
            session_question.setChoice_id(choice_Pre);
            ObjectId sectionId = new ObjectId(current_section.getId().toString());
            session_question.setTestId(test.getId());
            session_question.setCourseId(test.getCourseID());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            ObjectId user_id = userService.findByEmail(userName).getId();
            session_question.setUser_id(user_id);
            quizService.saveSession(session_question);
        }
        model.addAttribute("listChoice", listChoice);
        model.addAttribute("section", current_section.getId().toString());
        return "quiz";
    }

    public Question getRandomList(List<Question> list) {
        //0-4
        int index = random.nextInt(list.size());
        System.out.println("\nIndex :" + index);
        return list.get(index);
    }

}

