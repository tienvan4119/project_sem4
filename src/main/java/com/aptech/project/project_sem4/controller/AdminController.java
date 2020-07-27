package com.aptech.project.project_sem4.controller;

import com.aptech.project.project_sem4.model.*;
import com.aptech.project.project_sem4.model.Class;
import com.aptech.project.project_sem4.service.AdminService;
import com.aptech.project.project_sem4.service.QuizService;
import com.aptech.project.project_sem4.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

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
    public List<Class> AddNewTeacher(HttpServletRequest request, ModelAndView modelAndView)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        String pass = bCryptPasswordEncoder.encode("Van@411209");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String userEmail = request.getParameter("userEmail");
        String userFaculty = request.getParameter("userFaculty");
        String classID = request.getParameter("userClassID");
        ObjectId classID_convert = new ObjectId(classID);
//        User userExits = userService.findByEmail(userEmail);
        List<Faculty> listFaculty = adminService.getAllFaculty();
        List <User> list_AllUser = userService.listAll();

        List<User> listTeacher = new ArrayList<User>();

        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("TEACHER"))
            {
                listTeacher.add(list_AllUser.get(i));
            }
        }
        List<Class> listClass = adminService.getAllClass();

        System.out.println(pass);
        User new_teacher = new User();
        new_teacher.setFirstName(firstName);
        new_teacher.setLastName(lastName);
        new_teacher.setPassword(pass);
        new_teacher.setEmail(userEmail);
        new_teacher.setClassId(classID_convert);
        adminService.saveTeacher(new_teacher);
//        String pass = bCryptPasswordEncoder.encode(request.getParameter("userPass"));

        return listClass;
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
        //model.addAttribute("listTopic", listTopic);
        return quizService.listAllTopicBySection_id(section_id);
    }
    @RequestMapping(value= "/getListCourses",  method = RequestMethod.POST)
    public List<Course> getListSubject(Model model, HttpServletRequest request)
    {
        String teacher_email = request.getParameter("teacher_email");
        String teacher_id = adminService.getUserbyEmail(teacher_email).getId().toString();

        return adminService.getListCourseOfTeacher(teacher_id);
    }

    @RequestMapping(value = "/getListStudentofCourse", method = RequestMethod.POST)
    public List<User> getListStudentofCourse(Model model, HttpServletRequest request)
    {
        String course_name = request.getParameter("course_name");
        String course_id = adminService.getCoursebyName(course_name).getId().toString();
        List<User> listUser = new ArrayList<>();
        List<RelationStudentCourse> listCourseStudent  = adminService.getListCourseAndStudent(course_id);
        for(int i = 0; i< listCourseStudent.size(); i++)
        {
            listUser.add(adminService.findUserbyId(listCourseStudent.get(i).getStudentID().toString()));
        }
        return listUser;
    }

    @RequestMapping(value = "/getListStudentofCourseClass", method = RequestMethod.POST)
    public List<Class> getListClassStudent(Model model, HttpServletRequest request)
    {
        String course_name = request.getParameter("course_name");
        String course_id = adminService.getCoursebyName(course_name).getId().toString();
        List<User> listUser = new ArrayList<>();
        List<RelationStudentCourse> listCourseStudent  = adminService.getListCourseAndStudent(course_id);
        for(int i = 0; i< listCourseStudent.size(); i++)
        {
            listUser.add(adminService.findUserbyId(listCourseStudent.get(i).getStudentID().toString()));
        }
        List<Class> listClassofStudent = new ArrayList<>();
        for(int j = 0; j< listUser.size(); j++) {
            listClassofStudent.add(adminService.findClassById(listUser.get(j).getClassId().toString()));
        }
        return listClassofStudent;
    }

    @RequestMapping(value = "/getListTeacherofFaculty", method = RequestMethod.POST)
    public List<User> getListTeacherofFaculty(Model model, HttpServletRequest request)
    {
        String faculty_id = request.getParameter("faculty_id");
        List<Class> listClassofFaculty = adminService.findClassesByFaculty(faculty_id);
        List <User> list_AllUser = userService.listAll();
        List <Role> listRole = userService.listAllRole();
        List<User> listUser = new ArrayList<User>();
        List<User> listTeacher = new ArrayList<User>();
        List<Class> listClassofTeacher = new ArrayList<>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("TEACHER"))
            {
                for(int j = 0; j< listClassofFaculty.size(); j++) {
                    if (list_AllUser.get(i).getClassId().equals(listClassofFaculty.get(j).getId())) {
                        listTeacher.add(list_AllUser.get(i));
                        break;
                    }
                }
            }
        }
        return listTeacher;
    }

    @RequestMapping(value = "/getListResultofCourse", method = RequestMethod.POST)
    public List<Result> getListResultofCourse(HttpServletRequest request)
    {
        String course_name = request.getParameter("course_name");
        List<Result> listResult = adminService.getFullResultofCourse(adminService.getCoursebyName(course_name).getId().toString());
        return listResult;
    }

    @RequestMapping(value = "/getListStudentbyResultofCourse", method = RequestMethod.POST)
    public List<User> getListStudentbyResultofCourse(HttpServletRequest request)
    {
        String course_name = request.getParameter("course_name");
        List<Result> listResult = adminService.getFullResultofCourse(adminService.getCoursebyName(course_name).getId().toString());
        List<User> listUser = new ArrayList<>();
        for(int i = 0; i<listResult.size();i++)
        {
            listUser.add(adminService.findUserbyId(listResult.get(i).getUser_id().toString()));
        }
        return listUser;
    }

    @RequestMapping(value = "/getListClassbyResultofCourse", method = RequestMethod.POST)
    public List<Class> getListClassbyResultofCourse(HttpServletRequest request)
    {
        String course_name = request.getParameter("course_name");
        List<Result> listResult = adminService.getFullResultofCourse(adminService.getCoursebyName(course_name).getId().toString());
        List<User> listUser = new ArrayList<>();
        for(int i = 0; i<listResult.size();i++)
        {
            listUser.add(adminService.findUserbyId(listResult.get(i).getUser_id().toString()));
        }
        List<Class> listClass = new ArrayList<>();
        for(int j = 0; j< listUser.size(); j++) {
            listClass.add(adminService.findClassById(listUser.get(j).getClassId().toString()));
        }
        return listClass;
    }

    @RequestMapping(value = "/getListTestbyResultofCourse", method = RequestMethod.POST)
    public List<Test> getListTestbyResultofCourse(HttpServletRequest request)
    {
        String course_name = request.getParameter("course_name");
        List<Result> listResult = adminService.getFullResultofCourse(adminService.getCoursebyName(course_name).getId().toString());
        List<Test> listTest = new ArrayList<>();
        for(int i = 0;i < listResult.size();i++)
        {
            listTest.add(adminService.getTestbyId(listResult.get(i).getTestId().toString()));
        }

        return listTest;
    }

    @RequestMapping(value = "/getListStringTest", method = RequestMethod.POST)
    public List<String> getListStringTest(HttpServletRequest request)
    {
        String course_name = request.getParameter("course_name");
        List<Result> listResult = adminService.getFullResultofCourse(adminService.getCoursebyName(course_name).getId().toString());
        List<Test> listTest = new ArrayList<>();
        for(int i = 0;i < listResult.size();i++)
        {
            listTest.add(adminService.getTestbyId(listResult.get(i).getTestId().toString()));
        }
        List<String> listString = new ArrayList<>();
        for(int i = 0; i< listTest.size();i++)
        {
            listString.add(listTest.get(i).getId().toString());
        }
        return listString;
    }

    @RequestMapping(value = "/setTestAgain", method = RequestMethod.POST)
    public List<Result> getListResultAfterTestAgain(HttpServletRequest request)
    {
        String student_email = request.getParameter("student_email");
        String course_name = request.getParameter("course_name");
        String[] dataTest = student_email.split("/");
        Result result = adminService.getResultbyTestAndUser(dataTest[1],adminService.getUserbyEmail(dataTest[0]).getId().toString());
        result.setDone(false);
        result.setTestAgain(true);
        quizService.saveResult(result);
        List<Result> listResult = adminService.getFullResultofCourse(adminService.getCoursebyName(course_name).getId().toString());
        return listResult;
    }

    @RequestMapping(value = "/getClassofFaculty", method = RequestMethod.POST)
    public List<Class> getListClassofFaculty(Model model, HttpServletRequest request)
    {
        String faculty_id = request.getParameter("faculty_id");
        List<Class> listClassofFaculty = adminService.findClassesByFaculty(faculty_id);
        List <User> list_AllUser = userService.listAll();
        List <Role> listRole = userService.listAllRole();
        List<User> listUser = new ArrayList<User>();
        List<User> listTeacher = new ArrayList<User>();
        List<Class> listClassofTeacher = new ArrayList<>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("TEACHER"))
            {
                for(int j = 0; j< listClassofFaculty.size(); j++) {
                    if (list_AllUser.get(i).getClassId().equals(listClassofFaculty.get(j).getId())) {
                        listTeacher.add(list_AllUser.get(i));
                        break;
                    }
                }
            }
        }
        for(int i = 0; i<listTeacher.size();i++)
        {
            listClassofTeacher.add(adminService.findClassById(listTeacher.get(i).getClassId().toString()));
        }
        return listClassofTeacher;
    }

    @RequestMapping(value = "/GetFullTeacher", method = RequestMethod.POST)
    public List<User> getTeacherFull(Model model, HttpServletRequest request)
    {
        List <User> list_AllUser = userService.listAll();
        List <Role> listRole = userService.listAllRole();
        List<User> listUser = new ArrayList<User>();
        List<User> listTeacher = new ArrayList<User>();
        List<Class> listClassofTeacher = new ArrayList<>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("TEACHER"))
            {
                listTeacher.add(list_AllUser.get(i));
            }
        }
        return listTeacher;
    }

    @RequestMapping(value = "/GetFullClass", method = RequestMethod.POST)
    public List<Class> getClassFull(Model model, HttpServletRequest request)
    {
        List <User> list_AllUser = userService.listAll();
        List <Role> listRole = userService.listAllRole();
        List<User> listUser = new ArrayList<User>();
        List<User> listTeacher = new ArrayList<User>();
        List<Class> listClassofTeacher = new ArrayList<>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("TEACHER"))
            {
                listTeacher.add(list_AllUser.get(i));
                String classID = list_AllUser.get(i).getClassId().toString();
                listClassofTeacher.add(adminService.findClassById(list_AllUser.get(i).getClassId().toString()));
            }
        }
        return listClassofTeacher;
    }

    @RequestMapping(value = "/addQuizTest", method = RequestMethod.GET)
    public List<Test> AddTestQuiz(Model model, HttpServletRequest request)
    {
        String className = request.getParameter("Class");
        String soCau = request.getParameter("SoCau");
        String time = request.getParameter("ThoiGian");
        String typeTest = request.getParameter("testType");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        int timeTest = Integer.parseInt(time);
        Course currentCourse = adminService.getCoursebyName(className);
        Test newTestQuiz = new Test();
        newTestQuiz.setTime(timeTest);
        newTestQuiz.setCourseID(currentCourse.getId());
        newTestQuiz.setTeacherID(user.getId());
        int numberQuestion = Integer.parseInt(soCau);
        newTestQuiz.setNumberQuestion(numberQuestion);
        newTestQuiz.setTestType(typeTest);
        adminService.saveQuizTest(newTestQuiz);
        List<User> listUser = new ArrayList<>();
        List<RelationStudentCourse> listCourseStudent  = adminService.getListCourseAndStudent(currentCourse.getId().toString());
        for(int i = 0; i< listCourseStudent.size(); i++)
        {
            listUser.add(adminService.findUserbyId(listCourseStudent.get(i).getStudentID().toString()));
        }
        for(int i = 0; i<listUser.size(); i++)
        {
            Result result = new Result();
            result.setMark(0);
            result.setCourseId(currentCourse.getId());
            result.setTestId(newTestQuiz.getId());
            result.setUser_id(listUser.get(i).getId());
            result.setDone(false);
            result.setTestAgain(false);
            result.setTime(timeTest * 60);
            quizService.saveResult(result);
        }
        return adminService.getListTest(user.getId().toString());
    }

    @RequestMapping(value = "/getFullTest", method = RequestMethod.POST)
    public List<Test> getListTest(Model model, HttpServletRequest request)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        return adminService.getListTest(user.getId().toString());
    }

    @RequestMapping(value = "/getClassTest", method = RequestMethod.GET)
    public List<Course> getFullCourseTest()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Test> listTest = adminService.getListTest(user.getId().toString());
        List<Course> listCourse = new ArrayList<>();
        for(int i = 0; i<listTest.size(); i++)
        {
            listCourse.add(adminService.findCoursebyID(listTest.get(i).getCourseID().toString()));
        }
        return listCourse;
    }

    @RequestMapping(value = "/getIDTest", method = RequestMethod.GET)
    public List<String> getIDTest()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Test> listTest = adminService.getListTest(user.getId().toString());
        List<String> listID = new ArrayList<>();
        for(int i = 0; i<listTest.size(); i++)
        {
            listID.add(listTest.get(i).getId().toString());
        }
        return listID;
    }
//    @RequestMapping(value = "/api/admin/getListTeacher", method = RequestMethod.POST)
//    public List<User> getListTeacher(Model model, HttpServletRequest request)
//    {
//        String section_id_value = request.getParameter("section_id_value");
//
//    }

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

    @RequestMapping(value = "/createQuestion", method = RequestMethod.POST)
    public Question createQuestion(HttpServletRequest request) {
        String questionDesc = request.getParameter("questionDesc");
        String title = request.getParameter("topicName");
        String sectionID = request.getParameter("sectionID");
        String rightChoice = request.getParameter("rightChoice");
        String choiceString = request.getParameter("choiceString");
        String[] choiceDesc = choiceString.split("\\,", -1);

        Question new_Question = new Question();

        Choice new_RightChoice = new Choice();
        new_Question.setTopic_id(adminService.getTopicByTitleAndSectionId(title, sectionID).getId());
        new_Question.setQuestion_desc(questionDesc);
        adminService.saveQuestion(new_Question);

        new_RightChoice.setChoice_desc(rightChoice);
        new_RightChoice.setQuestion_id(new_Question.getId());
        new_RightChoice.setCorrect(true);
        adminService.saveChoice(new_RightChoice);

        for (int j = 0; j < choiceDesc.length; j++) {
            Choice new_Choice = new Choice();
            new_Choice.setCorrect(false);
            new_Choice.setChoice_desc(choiceDesc[j]);
            new_Choice.setQuestion_id(new_Question.getId());
            adminService.saveChoice(new_Choice);
        }
        return new_Question;
    }
    @RequestMapping(value = "/getQuestionBySection", method = RequestMethod.POST)
    public List<Question> getQuestionBySection(HttpServletRequest request) {
        String section_id = request.getParameter("section_id");
        List<Topic> listTopic = quizService.listAllTopicBySection_id(section_id);
        List<Question> listQuestionyByTopic = new ArrayList<Question>();
        for (int i = 0; i < listTopic.size(); i++) {
            listQuestionyByTopic.addAll(quizService.listQuestionByTopic(listTopic.get(i).getId().toString()));
        }
        return listQuestionyByTopic;
    }
    @RequestMapping(value = "/getQuestionTopicBySection", method = RequestMethod.POST)
    public List<Topic> getQuestionTopicBySection(HttpServletRequest request) {

        String section_id = request.getParameter("section_id");
        List<Topic> listTopic = quizService.listAllTopicBySection_id(section_id);
        List<Question> listQuestionyByTopic = new ArrayList<Question>();
        for (int i = 0; i < listTopic.size(); i++) {
            listQuestionyByTopic.addAll(quizService.listQuestionByTopic(listTopic.get(i).getId().toString()));
        }
        List<Topic> getTopic = new ArrayList<>();
        for(int i = 0;i<listQuestionyByTopic.size();i++)
        {
            getTopic.add(quizService.getSectionId(listQuestionyByTopic.get(i).getTopic_id().toString()));
        }
        return getTopic;
    }

    @RequestMapping(value = "/DeleteTest", method = RequestMethod.POST)
    public List<Test> deleteTest(Model model, HttpServletRequest request)
    {
        String test_id  = request.getParameter("Test_id");

        Test test = adminService.getTestbyId(test_id);
        List<User> listUser = new ArrayList<>();
        List<RelationStudentCourse> listCourseStudent  = adminService.getListCourseAndStudent(test.getCourseID().toString());
        for(int i = 0; i< listCourseStudent.size(); i++)
        {
            listUser.add(adminService.findUserbyId(listCourseStudent.get(i).getStudentID().toString()));
        }
        for(int i = 0; i<listUser.size();i++)
        {
            Result result = adminService.getResultbyTestAndUser(test_id, listUser.get(i).getId().toString());
            adminService.DeleteResult(result);
        }
        adminService.DeleteTest(test_id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Test> listTest = adminService.getListTest(user.getId().toString());
        return listTest;
    }

    @RequestMapping(value = "/getTest", method = RequestMethod.POST)
    public Test dataTest(Model model, HttpServletRequest request)
    {
        String test_id  = request.getParameter("test_id");
        return adminService.getTestbyId(test_id);
    }
    @RequestMapping(value = "/getClassName", method = RequestMethod.GET)
    public Course getClassName(Model model, HttpServletRequest request)
    {
        String test_id  = request.getParameter("test_id");
        return adminService.findCoursebyID(adminService.getTestbyId(test_id).getCourseID().toString());
    }
    @RequestMapping(value = "/getClassTestEdit", method = RequestMethod.GET)
    public List<Course> getListEdit(Model model, HttpServletRequest request)
    {
        String test_id  = request.getParameter("test_id");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Course> listCourse = adminService.getListCourseOfTeacher(user.getId().toString());

        Course dataCurrentCourse = adminService.findCoursebyID(adminService.getTestbyId(test_id).getCourseID().toString());
        for (int i = 0; i<listCourse.size(); i++)
        {
            if(listCourse.get(i).getId().equals(dataCurrentCourse.getId()))
            {
                listCourse.remove(listCourse.get(i));
                break;
            }
        }
        return listCourse;
    }
    @RequestMapping(value = "/editTest", method = RequestMethod.POST)
    public List<Test> listTest(Model model, HttpServletRequest request)
    {
        String test_id  = request.getParameter("test_id");
        String className  = request.getParameter("ClassName");
        String numberQuestion  = request.getParameter("SoCau");
        String time  = request.getParameter("ThoiGian");
        Test testEdit = adminService.getTestbyId(test_id);
        int timeTest = Integer.parseInt(time);
        int soCau = Integer.parseInt(numberQuestion);
        Course currentCourse = adminService.getCoursebyName(className);
        testEdit.setNumberQuestion(soCau);
        testEdit.setCourseID(currentCourse.getId());
        testEdit.setTime(timeTest);
        adminService.saveQuizTest(testEdit);
        List<Result> listResult = adminService.getListResultByTestId(testEdit.getId().toString());
        for(int i = 0; i<listResult.size();i++)
        {
            listResult.get(i).setTime(timeTest*60);
            quizService.saveResult(listResult.get(i));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Test> listTest = adminService.getListTest(user.getId().toString());
        return listTest;
    }
    @RequestMapping(value = "/addSection", method = RequestMethod.POST)
    public String addNewSection(HttpServletRequest request)
    {
        String sectionName = request.getParameter("sectionName");
        String facultyID = request.getParameter("facultyID");
        ObjectId facultyID_Convert = new ObjectId(facultyID);
        Section new_Section = new Section();
        new_Section.setTitle(sectionName);
        new_Section.setFaculty_id(facultyID_Convert);
        adminService.saveSection(new_Section);
        return "Yes";
    }

    @RequestMapping(value = "/AddNewStudent", method = RequestMethod.POST)
    public String AddNewStudent(HttpServletRequest request, ModelAndView modelAndView, @RequestParam Map<String, String> requestParams)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String userEmail = request.getParameter("userEmail");
        String userPass = "Van@411209";
        String address = request.getParameter("address");
        String classID = request.getParameter("userClassID");
        String courseID = request.getParameter("userCourseID");
        ObjectId classID_convert = new ObjectId(classID);

        String pass = bCryptPasswordEncoder.encode(userPass);
        System.out.println(pass);
        User new_Student = new User();
        new_Student.setFirstName(firstName);
        new_Student.setLastName(lastName);
        new_Student.setPassword(pass);
        new_Student.setEmail(userEmail);
        new_Student.setClassId(classID_convert);
        new_Student.setLocation(address);
        adminService.saveUser(new_Student);
        RelationStudentCourse newCourseStudent = new RelationStudentCourse();
        newCourseStudent.setCourseID(new ObjectId(courseID));
        newCourseStudent.setStudentID(new_Student.getId());

        adminService.saveStudentCourse(newCourseStudent);


        return "Yes";
    }

    @RequestMapping(value = "/addTopic", method = RequestMethod.POST)
    public String addNewTopic(HttpServletRequest request)
    {
        String topicName = request.getParameter("topicName");
        String sectionID = request.getParameter("sectionID");
        ObjectId sectionID_Convert = new ObjectId(sectionID);
        Topic new_Topic = new Topic();
        new_Topic.setTitle(topicName);
        new_Topic.setSectionId(sectionID_Convert);
        adminService.saveTopic(new_Topic);
        return "Yes";
    }

    @RequestMapping(value = "/getQuestionById", method = RequestMethod.POST)
    public Question getQuestionById(Model model, HttpServletRequest request) {
        String question_id = request.getParameter("question_id");
        return adminService.getQuestionById(question_id);
    }
    @RequestMapping(value = "/getChoiceByQuestionDesc", method = RequestMethod.POST)
    public List<Choice> getChoiceByQuestionDesc(Model model, HttpServletRequest request) {
        String question_desc = request.getParameter("question_desc");
        Question edit_question = adminService.findQuestionByDesc(question_desc);
        return adminService.findChoiceByQuestionId(edit_question.getId().toString());
    }

    @RequestMapping(value = "/getTeacherByEmail", method = RequestMethod.POST)
    public User getTeacherByEmail(Model model, HttpServletRequest request)
    {
        String userEmail = request.getParameter("teacher_email");
        return adminService.getUserbyEmail(userEmail);
    }
    @RequestMapping(value = "/getTeacherClassByEmail", method = RequestMethod.POST)
    public Class getTeacherClassByEmail(Model model, HttpServletRequest request)
    {
        String userEmail = request.getParameter("teacher_email");
        return adminService.findClassById(adminService.getUserbyEmail(userEmail).getClassId().toString());
    }

    @RequestMapping(value = "/getTeacherOtherClass", method = RequestMethod.POST)
    public List<Class> getListotherClass(Model model, HttpServletRequest request)
    {
        String userEmail = request.getParameter("teacher_email");
        Class userClass = adminService.findClassById(adminService.getUserbyEmail(userEmail).getClassId().toString());
        List<Class> listClass = adminService.getAllClass();
        for(int i=0; i<listClass.size();i++)
        {
            if(listClass.get(i).getId().equals(userClass.getId()))
            {
                listClass.remove(listClass.get(i));
                break;
            }
        }
        return listClass;
    }

    @RequestMapping(value = "/editTeacher", method = RequestMethod.POST)
    public Class editTeacher(HttpServletRequest request)
    {
        String user_email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String className = request.getParameter("className");
        String location = request.getParameter("location");
        User choice_user = adminService.getUserbyEmail(user_email);
        choice_user.setClassId(adminService.findClassByName(className).getId());
        choice_user.setFirstName(firstName);
        choice_user.setLastName(lastName);
        choice_user.setLocation(location);
        adminService.saveTeacherEdit(choice_user);
        return adminService.findClassById(adminService.getUserbyEmail(user_email).getClassId().toString());
    }

    @RequestMapping(value = "/DeleteTeacher", method = RequestMethod.POST)
    public List<Faculty> deleteTeacher(HttpServletRequest request)
    {
        String user_email = request.getParameter("teacher_email");
        String course_name = request.getParameter("course_name");
        User choice_user = adminService.getUserbyEmail(user_email);
        Set role = choice_user.getRoles();
        Object[] roles = role.toArray();
        Role role_name = (Role) roles[0];
        if(role_name.getRole().equals("USER"))
        {
            RelationStudentCourse studentCourse = adminService.getRelationCourseStudent(adminService.getCoursebyName(course_name).getId().toString(), choice_user.getId().toString());
            adminService.DeleteRelationCourseStudent(studentCourse);
        }
        adminService.DeleteTeacher(choice_user);
        return adminService.getAllFaculty();
    }
    @RequestMapping(value = "/getStudentByEmail", method = RequestMethod.POST)
    public User getStudentByEmail(Model model, HttpServletRequest request)
    {
        String userEmail = request.getParameter("student_email");
        return adminService.getUserbyEmail(userEmail);
    }
    @RequestMapping(value = "/getStudentClassByEmail", method = RequestMethod.POST)
    public Class getStudentClassByEmail(Model model, HttpServletRequest request)
    {
        String userEmail = request.getParameter("student_email");
        return adminService.findClassById(adminService.getUserbyEmail(userEmail).getClassId().toString());
    }
    @RequestMapping(value = "/editStudent", method = RequestMethod.POST)
    public List<Faculty> editStudent(HttpServletRequest request)
    {
        String user_email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String location = request.getParameter("location");
        String courseName = request.getParameter("courseName");
        String preCourseName = request.getParameter("preCourseName");
        String mssv = request.getParameter("mssv");
        User choice_user = adminService.getUserbyEmail(user_email);
        choice_user.setFirstName(firstName);
        choice_user.setLastName(lastName);
        choice_user.setLocation(location);
        choice_user.setMSSV(mssv);
        adminService.saveStudentEdit(choice_user);

        RelationStudentCourse studentCourse = adminService.getRelationCourseStudent(adminService.getCoursebyName(preCourseName).getId().toString(),choice_user.getId().toString());
        studentCourse.setCourseID(adminService.getCoursebyName(courseName).getId());
        adminService.saveStudentCourse(studentCourse);
        return adminService.getAllFaculty();
    }
    @RequestMapping(value = "/DeleteStudent", method = RequestMethod.POST)
    public List<Faculty> deleteStudent(HttpServletRequest request)
    {
        String user_email = request.getParameter("student_email");
        String preCourseName = request.getParameter("preCourseName");
        User choice_user = adminService.getUserbyEmail(user_email);
        adminService.DeleteStudent(choice_user);
        RelationStudentCourse studentCourse = adminService.getRelationCourseStudent(adminService.getCoursebyName(preCourseName).getId().toString(),choice_user.getId().toString());
        adminService.DeleteRelationCourseStudent(studentCourse);
        return adminService.getAllFaculty();
    }
    @RequestMapping(value = "/getTime", method = RequestMethod.POST)
    public Test getTest(HttpServletRequest request)
    {
        String test_id = request.getParameter("test_id");
        Test test = adminService.getTestbyId(test_id);
        return test;
    }

    @RequestMapping(value = "/DeleteQuestion", method = RequestMethod.POST)
    public List<Faculty> deleteQuestion(HttpServletRequest request) {
        String question_desc = request.getParameter("question_desc");
        Question delete_question = adminService.findQuestionByDesc(question_desc);
        List<Choice> choice = adminService.findChoiceByQuestionId(delete_question.getId().toString());
        for(int i = 0 ; i < choice.size(); i++)
        {
            adminService.DeleteChoice(choice.get(i));
        }
        adminService.DeleteQuestion(delete_question);
        return adminService.getAllFaculty();
    }

    @RequestMapping(value = "/getStatisticTeacherAdmin", method = RequestMethod.POST)
    public List<Integer> listIntTeacher(Model model, HttpServletRequest request)
    {
        List<Faculty> listFaculty = adminService.getAllFaculty();
        List <User> list_AllUser = userService.listAll();
        List<User> listTeacher = new ArrayList<User>();
        List<Integer> listIntTeacher = new ArrayList<>();
        List<Class> listClassofTeacher = new ArrayList<>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("TEACHER"))
            {
                listTeacher.add(list_AllUser.get(i));
            }
        }
        int number = 0;
        for(int i = 0; i< listFaculty.size();i++)
        {
            for(int k = 0; k<listTeacher.size();k++)
            {
                if(adminService.findClassById(listTeacher.get(k).getClassId().toString()).getFacultyId().equals(listFaculty.get(i).getId()))
                {
                    number++;
                }
            }
            listIntTeacher.add(number);
            number = 0;
        }
        return listIntTeacher;
    }

    @RequestMapping(value = "/getStatisticStudentAdmin", method = RequestMethod.POST)
    public List<Integer> listintStudent(Model model, HttpServletRequest request)
    {
        List<Faculty> listFaculty = adminService.getAllFaculty();
        List <User> list_AllUser = userService.listAll();
        List<User> listStudent = new ArrayList<User>();
        List<Integer> listIntTeacher = new ArrayList<>();
        List<Class> listClassofTeacher = new ArrayList<>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("USER"))
            {
                listStudent.add(list_AllUser.get(i));
            }
        }
        int number = 0;
        for(int i = 0; i< listFaculty.size();i++)
        {
            for(int k = 0; k<listStudent.size();k++)
            {
                if(adminService.findClassById(listStudent.get(k).getClassId().toString()).getFacultyId().equals(listFaculty.get(i).getId()))
                {
                    number++;
                }
            }
            listIntTeacher.add(number);
            number = 0;
        }
        return listIntTeacher;
    }


    @RequestMapping(value = "/getTimeOfTest", method = RequestMethod.POST)
    public Result getNumber(HttpServletRequest request)
    {
        String params = request.getParameter("current_url");
        String[] test_id = params.split("=");
        System.out.println(test_id[1]);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        return adminService.getResultbyTestAndUser(test_id[1], user.getId().toString());

    }

    @RequestMapping(value = "/getNumberofTest", method = RequestMethod.POST)
    public Test getNumberQuest(HttpServletRequest request)
    {
        String params = request.getParameter("current_url");
        String[] test_id = params.split("=");
        System.out.println(test_id[1]);

        return adminService.getTestbyId(test_id[1]);

    }

    @RequestMapping(value = "/getUserByEmail", method = RequestMethod.POST)
    public User getUserByEmail(HttpServletRequest request)
    {
        String email = request.getParameter("email");
        return adminService.getUserbyEmail(email);
    }
    @RequestMapping(value = {"/changeUserProfile"}, method = RequestMethod.POST)
    public User changeUserProfile(Model model, HttpServletRequest request)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String userEmail = request.getParameter("email");
        String userPass = request.getParameter("password");
        String mssv = request.getParameter("mssv");
        String location = request.getParameter("location");
        String pass = bCryptPasswordEncoder.encode(userPass);
        System.out.println(pass);
        User new_user = userService.findByEmail(userEmail);
        new_user.setFirstName(firstName);
        new_user.setLastName(lastName);
        new_user.setPassword(pass);
        new_user.setLocation(location);
        new_user.setMSSV(mssv);
        userService.saveUser(new_user);
        return new_user;
    }

    @RequestMapping(value = {"/changeAdminProfile"}, method = RequestMethod.POST)
    public User changeAdminProfile(Model model, HttpServletRequest request)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String userEmail = request.getParameter("email");
        String userPass = request.getParameter("password");
        String location = request.getParameter("location");
        String pass = bCryptPasswordEncoder.encode(userPass);
        System.out.println(pass);
        User new_user = userService.findByEmail(userEmail);
        new_user.setFirstName(firstName);
        new_user.setLastName(lastName);
        new_user.setPassword(pass);
        new_user.setLocation(location);
        adminService.saveAdminProfile(new_user);
        return new_user;
    }

    @RequestMapping(value = "/getSectionByTestId", method = RequestMethod.POST)
    public Test getSectionIdByTest(HttpServletRequest request)
    {
        String testId = request.getParameter("testID");
        return adminService.getTestbyId(testId);
    }

    @RequestMapping(value = "/getFullCourse", method = RequestMethod.POST)
    public List<Course> getFullCourse()
    {
        return adminService.getFullCourse();
    }

    @RequestMapping(value = "/getFullTeacherOfCourse", method = RequestMethod.POST)
    public List<User> getFullTeacherOfCourse()
    {
        List<Course> listCourse = adminService.getFullCourse();
        List<User> listUser = new ArrayList<>();
        for(int i=0; i< listCourse.size();i++)
        {
            listUser.add(adminService.findUserbyId(listCourse.get(i).getTeacherID().toString()));
        }
        return listUser;
    }

    @RequestMapping(value = "/addNewCourse", method = RequestMethod.POST)
    public List<Course> AddNewCourse(HttpServletRequest request)
    {
        String teacherID = request.getParameter("teacherID");
        String courseName = request.getParameter("courseName");
        String sectionID = request.getParameter("sectionID");
        Course newCourse = new Course();
        newCourse.setName(courseName);
        newCourse.setTeacherID(new ObjectId(teacherID));
        adminService.saveNewCourse(newCourse);
        RelationCourseSection courseSection = new RelationCourseSection();
        courseSection.setCourseId(newCourse.getId());
        courseSection.setSectionId(new ObjectId(sectionID));
        adminService.SaveCourseSection(courseSection);
        return adminService.getFullCourse();
    }

    @RequestMapping(value = "/getCurentCourse", method = RequestMethod.POST)
    public User getCurentCourse(HttpServletRequest request)
    {
        return adminService.findUserbyId(adminService.getCoursebyName(request.getParameter("courseName")).getTeacherID().toString());
    }

    @RequestMapping(value = "/getOtherTeacher", method = RequestMethod.POST)
    public List<User> getOtherTeacher(HttpServletRequest request)
    {
        List <User> list_AllUser = userService.listAll();
        List<User> listTeacher = new ArrayList<User>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("TEACHER"))
            {
                listTeacher.add(list_AllUser.get(i));
            }
        }
        for(int i = 0; i<listTeacher.size();i++)
        {
            if(listTeacher.get(i).getId().equals(adminService.findUserbyId(adminService.getCoursebyName(request.getParameter("courseName")).getTeacherID().toString()).getId()))
            {
                listTeacher.remove(i);
                break;
            }
        }
        return listTeacher;
    }

    @RequestMapping(value = "/getStringTeacherId", method = RequestMethod.POST)
    public List<String> getStringTeacherId(HttpServletRequest request)
    {
        User currentTeacher =  adminService.findUserbyId(adminService.getCoursebyName(request.getParameter("courseName")).getTeacherID().toString());
        List <User> list_AllUser = userService.listAll();
        List<User> listTeacher = new ArrayList<User>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("TEACHER"))
            {
                listTeacher.add(list_AllUser.get(i));
            }
        }
        for(int i = 0; i<listTeacher.size();i++)
        {
            if(listTeacher.get(i).getId().equals(adminService.findUserbyId(adminService.getCoursebyName(request.getParameter("courseName")).getTeacherID().toString()).getId()))
            {
                listTeacher.remove(i);
                break;
            }
        }
        List<String> listString = new ArrayList<>();
        listString.add(currentTeacher.getId().toString());
        for(int i=0; i<listTeacher.size();i++)
        {
            listString.add(listTeacher.get(i).getId().toString());
        }
        return listString;
    }

    @RequestMapping(value = "/editCourse", method = RequestMethod.POST)
    public List<Course> editCourse(HttpServletRequest request)
    {
        String teacherID = request.getParameter("teacherID");
        String courseName = request.getParameter("courseName");
        Course course = adminService.getCoursebyName(courseName);
        course.setTeacherID(new ObjectId(teacherID));
        adminService.saveNewCourse(course);
        return adminService.getFullCourse();
    }

    @RequestMapping(value = "/DeleteCourse", method = RequestMethod.POST)
    public List<Course> DeleteCourse(HttpServletRequest request)
    {
        String courseName = request.getParameter("courseName");

        Course course = adminService.getCoursebyName(courseName);
        try
        {
            List<RelationStudentCourse> studentCourses = adminService.getListCourseAndStudent(course.getId().toString());
            if(studentCourses != null)
            {
                for(int i = 0; i< studentCourses.size();i++)
                {
                    adminService.DeleteStudentOfCourse(studentCourses.get(i));
                }
            }
            return adminService.getFullCourse();
        }
        catch(Exception e)
        {

        }finally {
            adminService.DeleteCourse(course);
            RelationCourseSection courseSection = adminService.findbyCourseId(course.getId().toString());
            adminService.DeleteCourseSection(courseSection);
            return adminService.getFullCourse();
        }
    }

    @RequestMapping(value = "/addStudentByFile", method = RequestMethod.POST)
    public List<User> addStudentByFile(Model model, HttpServletRequest request)
    {
        String course_name = request.getParameter("course_name");
        String listData = request.getParameter("listData");
        String[] data = listData.split(",");
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        String pass = bCryptPasswordEncoder.encode("Van@411209");
        User user = new User();
        user.setEmail(data[0]);
        user.setFirstName(data[1]);
        user.setLastName(data[2]);
        Class getClass = adminService.findClassByName(data[3]);
        user.setClassId(getClass.getId());
        user.setMSSV(data[4]);
        user.setPassword(pass);
        userService.saveUser(user);
        RelationStudentCourse relationStudentCourse = new RelationStudentCourse();
        relationStudentCourse.setStudentID(user.getId());
        relationStudentCourse.setCourseID(adminService.getCoursebyName(course_name).getId());
        adminService.saveStudentCourse(relationStudentCourse);
        String course_id = adminService.getCoursebyName(course_name).getId().toString();
        List<User> listUser = new ArrayList<>();
        List<RelationStudentCourse> listCourseStudent  = adminService.getListCourseAndStudent(course_id);
        for(int i = 0; i< listCourseStudent.size(); i++)
        {
            listUser.add(adminService.findUserbyId(listCourseStudent.get(i).getStudentID().toString()));
        }
        return listUser;
    }

    @RequestMapping(value = "/addTeacherByFile", method = RequestMethod.POST)
    public List<User> addTeacherByFile(Model model, HttpServletRequest request)
    {
        String listData = request.getParameter("listData");
        String[] data = listData.split(",");
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        String pass = bCryptPasswordEncoder.encode("Van@411209");
        User user = new User();
        user.setEmail(data[0]);
        user.setFirstName(data[1]);
        user.setLastName(data[2]);
        Class getClass = adminService.findClassByName(data[3]);
        user.setClassId(getClass.getId());
        user.setPassword(pass);
        adminService.saveTeacher(user);
        List <User> list_AllUser = userService.listAll();
        List<User> listTeacher = new ArrayList<User>();
        for(int i = 0; i < list_AllUser.size(); i++)
        {
            Set role = list_AllUser.get(i).getRoles();
            Object[] roles = role.toArray();
            Role role_name = (Role) roles[0];
            if(role_name.getRole().equals("TEACHER"))
            {
                listTeacher.add(list_AllUser.get(i));
            }
        }
        return listTeacher;
    }

    @RequestMapping(value = "/addTeacherCourseByFile", method = RequestMethod.POST)
    public List<Course> addTeacherCourseByFile(Model model, HttpServletRequest request)
    {
        String listData = request.getParameter("listData");
        String[] data = listData.split(",");
        User user = adminService.getUserbyEmail(data[0]);
        Course course = new Course();
        course.setName(data[1]);
        course.setTeacherID(user.getId());
        adminService.saveNewCourse(course);
        RelationCourseSection courseSection = new RelationCourseSection();
        courseSection.setSectionId(new ObjectId(data[2]));
        courseSection.setCourseId(course.getId());
        adminService.saveCourseSection(courseSection);
        return adminService.getFullCourse();
    }

    @RequestMapping(value = "/getRightChoiceByQuestionId", method = RequestMethod.POST)
    public Choice getRightChoiceByQuestionId(Model model, HttpServletRequest request) {
        String question_id = request.getParameter("question_id");

        List<Choice> choices = adminService.findChoiceByQuestionId(question_id);
        for(int i = 0; i < choices.size(); i++)
        {
            if(choices.get(i).getCorrect())
            {
                return choices.get(i);
            }
        }
        return null;
    }

    @RequestMapping(value = "/getFalseChoiceByQuestionId", method = RequestMethod.POST)
    public List<Choice> getFalseChoiceByQuestionDesc(Model model, HttpServletRequest request) {
        String question_id = request.getParameter("question_id");

        List<Choice> choices = adminService.findChoiceByQuestionId(question_id);
        List<Choice> falseChoices = new ArrayList<>();
        for(int i = 0; i < choices.size(); i++)
        {
            if(!choices.get(i).getCorrect())
            {
                falseChoices.add(choices.get(i));
            }
        }
        return falseChoices;
    }

    @RequestMapping(value = "/editQuestion", method = RequestMethod.POST)
    public List<Faculty> editQuestion(HttpServletRequest request) {
        String question_desc = request.getParameter("question_desc");
        String rightChoice = request.getParameter("rightChoice");
        String choiceString = request.getParameter("choiceString");
        String[] choiceDesc = choiceString.split("\\,", -1);
        List<Choice> falseChoices = new ArrayList<>();

        Question edit_question = adminService.findQuestionByDesc(question_desc);
        List<Choice> choices = adminService.findChoiceByQuestionId(edit_question.getId().toString());
        for(int i = 0; i < choices.size(); i++)
        {
            if(choices.get(i).getCorrect() == true)
            {
                choices.get(i).setChoice_desc(rightChoice);
                adminService.saveChoice(choices.get(i));
            }
            else
            {
                falseChoices.add(choices.get(i));
            }
        }
        for(int i = 0; i < falseChoices.size(); i++)
        {
            falseChoices.get(i).setChoice_desc(choiceDesc[i]);
            adminService.saveChoice(falseChoices.get(i));
        }
        return adminService.getAllFaculty();
    }

    @RequestMapping(value = "/getStringIdOfQuestion", method = RequestMethod.POST)
    public List<String> getStringIdOfQuestion(HttpServletRequest request)
    {
        String section_id = request.getParameter("section_id");
        List<Topic> listTopic = quizService.listAllTopicBySection_id(section_id);
        List<Question> listQuestionyByTopic = new ArrayList<Question>();
        for (int i = 0; i < listTopic.size(); i++) {
            listQuestionyByTopic.addAll(quizService.listQuestionByTopic(listTopic.get(i).getId().toString()));
        }
        List<String> listString = new ArrayList<>();
        for(int i=0;i<listQuestionyByTopic.size();i++)
        {
            listString.add(listQuestionyByTopic.get(i).getId().toString());
        }
        return listString;
    }

    @RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
    public List<String> controllerrrrr(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        User user = adminService.getUserbyEmail(email);
        String data;
        if (user == null)
        {
            data = "Email Valid";

        }
        else {
            data = "Email Exited";

        }
        List<String> listString = new ArrayList<>();
        listString.add(data);
        return listString;
    }
}
