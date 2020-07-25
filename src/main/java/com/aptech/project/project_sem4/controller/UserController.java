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
        return "/Student/section";
    }

    @RequestMapping(value = {"/createQuiz"})
    public String createQuiz(Model model, HttpServletRequest request) {
        String params = request.getQueryString();
        String[] test_id = params.split("=");
        System.out.println(test_id[1]);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Result result = adminService.getResultbyTestAndUser(test_id[1],  userService.findByEmail(userName).getId().toString());

        if(result.isDone() && !result.isTestAgain())
        {
            return "redirect:/userProfile";
        }

        if(!result.isDone() && !result.isTestAgain())
        {
            result.setDone(true);
            quizService.saveResult(result);
            Section current_section = quizService.isDone(userService.getCourseSection(adminService.getTestbyId(test_id[1]).getCourseID().toString()).getSectionId().toString());

            List<Topic> listTopic = quizService.listAllTopicBySection_id(current_section.getId().toString());
            // model.addAttribute("listTopic", listTopic);

            List<Question> listQuestion = quizService.listAllQuestion();
            List<Question> listQuestionyByTopic = new ArrayList<Question>();

            UserController obj = new UserController();
            List<Question> newListQuestion = new ArrayList<Question>();
            Test test = adminService.getTestbyId(test_id[1]);
            List<Test> listTest = adminService.getListTestByID(test_id[1]);
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
                ObjectId user_id = userService.findByEmail(userName).getId();
                session_question.setUser_id(user_id);
                quizService.saveSession(session_question);
            }

            model.addAttribute("listChoice", listChoice);
            model.addAttribute("listTest", listTest);
            model.addAttribute("section", current_section.getId().toString());
        }
        else if (!result.isDone() && result.isTestAgain())
        {
            result.setDone(true);
            quizService.saveResult(result);
            Section current_section = quizService.isDone(userService.getCourseSection(adminService.getTestbyId(test_id[1]).getCourseID().toString()).getSectionId().toString());

            List<Session> getListSessionTestAgain = userService.getListSessionTestAgain(test_id[1], userService.findByEmail(userName).getId().toString());
            List<Question> listQuestion = new ArrayList<>();
            for(int i = 0; i< getListSessionTestAgain.size();i++)
            {
                listQuestion.add(quizService.getTopicId(getListSessionTestAgain.get(i).getQuestion_id().toString()));
            }
            List<Choice> listChoice = new ArrayList<Choice>();
            for (int i = 0; i < listQuestion.size(); i++) {
                String question_id = listQuestion.get(i).getId().toString();
                List<Choice> listChoiceOfEachQuestion = quizService.listChoiceByQuestion_id(question_id);
                listChoice.addAll(listChoiceOfEachQuestion);
                System.out.println(question_id);
            }
            Test test = adminService.getTestbyId(test_id[1]);
            List<Test> listTest = adminService.getListTestByID(test_id[1]);
            for(int i = 0; i<listQuestion.size(); i++)
            {
                Session session = userService.findAllSessionByQuestionAndTestAndUser(listQuestion.get(i).getId().toString(),test.getId().toString(), userService.findByEmail(userName).getId().toString());
                session.setChoice_id(new ObjectId());
                quizService.saveSession(session);
            }
            result.setDone(true);
            model.addAttribute("listChoice", listChoice);
            model.addAttribute("listTest", listTest);
            model.addAttribute("listQuestion", listQuestion);
            model.addAttribute("section", current_section.getId().toString());
        }
        return "/Student/quiz";
    }

    public Question getRandomList(List<Question> list) {
        //0-4
        int index = random.nextInt(list.size());
        System.out.println("\nIndex :" + index);
        return list.get(index);
    }
}

