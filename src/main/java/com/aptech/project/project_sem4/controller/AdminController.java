package com.aptech.project.project_sem4.controller;

import com.aptech.project.project_sem4.model.*;
import com.aptech.project.project_sem4.model.Class;
import com.aptech.project.project_sem4.service.AdminService;
import com.aptech.project.project_sem4.service.QuizService;
import com.aptech.project.project_sem4.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.validation.Valid;
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
        new_Admin.setClassId(classID_convert);
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
        adminService.saveQuizTest(newTestQuiz);

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

    @RequestMapping(value = "/DeleteTest", method = RequestMethod.POST)
    public List<Test> deleteTest(Model model, HttpServletRequest request)
    {
        String test_id  = request.getParameter("Test_id");
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
        User choice_user = adminService.getUserbyEmail(user_email);
        adminService.DeleteTeacher(choice_user);
        return adminService.getAllFaculty();
    }

    @RequestMapping(value = "/getTime", method = RequestMethod.POST)
    public Test getTest(HttpServletRequest request)
    {
        String test_id = request.getParameter("test_id");
        Test test = adminService.getTestbyId(test_id);
        return test;
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
}
