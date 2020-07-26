package com.aptech.project.project_sem4.controller;

import com.aptech.project.project_sem4.model.*;
import com.aptech.project.project_sem4.model.Class;
import com.aptech.project.project_sem4.service.AdminService;
import com.aptech.project.project_sem4.service.QuizService;
import com.aptech.project.project_sem4.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/quiz")
public class SessionController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @RequestMapping(value = "/addChoice", method = RequestMethod.POST)
    public @ResponseBody String addChoice(HttpServletRequest request) {
        String choice_id = request.getParameter("getChoiceId");
        String param = request.getParameter("current_url");
        String[] test_id = param.split("=");
        String testId = test_id[1];

        ObjectId question_id = quizService.getChoiceById(choice_id).getQuestion_id();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ObjectId topic_id = quizService.getTopicId(question_id.toString()).getTopic_id();
        ObjectId section_id = quizService.getSectionId(topic_id.toString()).getSectionId();

        ObjectId choice_id_conver = new ObjectId(choice_id);
        System.out.println(authentication.getName());
        String userName = authentication.getName();
        ObjectId user_id = userService.findByEmail(userName).getId();
        Test test = adminService.getTestbyId(testId);
        Session sessionOfQues_exited = quizService.getSessionExit(question_id.toString());
        if(sessionOfQues_exited == null)
        {
            Session session = new Session();
            session.setUser_id(user_id);
            session.setQuestion_id(question_id);
            session.setChoice_id(choice_id_conver);
            session.setTestId(new ObjectId(testId));
            session.setCourseId(test.getCourseID());
            quizService.saveSession(session);
            System.out.println("choice id: "+choice_id +"   question id:   " + question_id + "     user id :   " + user_id);
        }
        else {
            sessionOfQues_exited.setChoice_id(choice_id_conver);
            quizService.saveSession(sessionOfQues_exited);
        }
        String ajaxResponse = "";
        return ajaxResponse;
    }

    @RequestMapping(value = "/finishTest", method = RequestMethod.POST)
    public @ResponseBody void finishTest(HttpServletRequest request)
    {
        String section_id = request.getParameter("getSectionId");
        String timeTest = request.getParameter("timeTest");
        String param = request.getParameter("current_url");
        String[] test_id = param.split("=");
        String[] timeArray = timeTest.split(" ");
        String[] time = timeArray[0].split("m");
        String testId = test_id[1];
        int timeRealMinute = Integer.parseInt(time[0]);
        String[] timeSecond = timeArray[1].split("s");
        int timeRealSecond = Integer.parseInt(timeSecond[0]);
        Test test = adminService.getTestbyId(testId);
        ObjectId section_id_conver = new ObjectId(section_id);
        int RealTime = timeRealMinute*60 + timeRealSecond;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        ObjectId user_id = userService.findByEmail(userName).getId();
        List<Session> listChoice = quizService.getListChoice(testId, user_id.toString());
        double mark = 0;
        try {
            for(int i = 0; i< test.getNumberQuestion(); i++)
            {
                String choice_can_check = listChoice.get(i).getChoice_id().toString();
//                Answer correct_answer = quizService.checkChoice(listChoice.get(i).getQuestion_id().toString());
                if(quizService.choice_chosen(choice_can_check) == null)
                {
                    System.out.println("abc");
                }
                else if(quizService.choice_chosen(choice_can_check).getCorrect())
                {
                    mark++;
                }
//                String correct_answer_id = correct_answer.getAnswer_id().toString();
//                if (choice_can_check.equals(correct_answer_id))
//                {
//                    mark++;
//                }
            }
            double rightMark = (mark/test.getNumberQuestion())*10;
            Result result = adminService.getResultbyTestAndUser(test.getId().toString(), user_id.toString());
            result.setUser_id(user_id);
            result.setTestId(test.getId());
            result.setCourseId(test.getCourseID());

            result.setTestAgain(false);
            result.setMark(rightMark);
            result.setTime(RealTime);
            quizService.saveResult(result);
        }catch (Exception e) {
            e.getMessage();
            System.out.println("nullllllllllll");
        }
    }

    @RequestMapping(value = {"/Result"}, method = RequestMethod.POST)
    public ModelAndView afterTest(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User current_user = userService.findByEmail(userName);
        Class user_class = userService.findClassByID(current_user.getClassId().toString());
        List<RelationStudentCourse> listCourseofStudent = adminService.getListCourseofStudent(current_user.getId().toString());
        List<Course> listCourse = new ArrayList<>();
        for(int i = 0; i < listCourseofStudent.size();i++)
        {
            listCourse.add(adminService.findCoursebyID(listCourseofStudent.get(i).getCourseID().toString()));
        }
        model.addAttribute("listCourse", listCourse);
        model.addAttribute("current_user", current_user);
        model.addAttribute("user_class", user_class);
        return new ModelAndView("redirect:/userProfile");
    }
}
