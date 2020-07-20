package com.aptech.project.project_sem4.service;

import com.aptech.project.project_sem4.model.*;
import com.aptech.project.project_sem4.model.Class;
import com.aptech.project.project_sem4.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service("adminService")
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChoiceRepository choiceRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private RelationStudentCourseRepository relationStudentCourseRepository;
    @Autowired
    private TestRepository testRepository;
    public User findUserbyId(String id)
    {
        return userRepository.findUserById(id);
    }
    public Class findClassById(String id)
    {
        return classRepository.findClassById(id);
    }
    public void saveAdmin(User user) {
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    public void saveTeacher(User user) {
        Role userRole = roleRepository.findByRole("TEACHER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    public void saveQuizTest(Test test) {
        testRepository.save(test);
    }

    public List<Test> getListTest()
    {
        return testRepository.findAll();
    }

    public Course findCoursebyID(String course_id)
    {
        return courseRepository.findCourseByID(course_id);
    }

    public void saveUser(User user) {
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    public List<Section> getAllSection()
    {
        return sectionRepository.findAll();
    }
    public Topic getTopicByTitleAndSectionId(String title, String section_id)
    {
        return topicRepository.findTopicByTitleAndSectionId(title,section_id);
    }
    public void saveQuestion(Question question)
    {
        questionRepository.save(question);
    }
    public void saveChoice(Choice choice)
    {
        choiceRepository.save(choice);
    }
    public void saveAnswer(Answer answer)
    {
        answerRepository.save(answer);
    }
    public Choice findChoiceByChoiceDesc(String choice_desc)
    {
        return choiceRepository.findChoiceByChoice_desc(choice_desc);
    }
    public List<Faculty> getAllFaculty()
    {
        return facultyRepository.findAll();
    }
    public List<Section> getListSubject(String faculty_id)
    {
        return sectionRepository.getListSubject(faculty_id);
    }
    public List<Course> getListCourseOfTeacher(String teacherID)
    {
        return courseRepository.findCoursesByTeacherID(teacherID);
    }

    public List<Class> findClassesByFaculty(String facultyID)
    {
        return classRepository.findClassesByFacultyId(facultyID);
    }
    public User getUserbyEmail(String email)
    {
        return  userRepository.findByEmail(email);
    }
    public List<RelationStudentCourse> getListCourseAndStudent(String courseID)
    {
        return relationStudentCourseRepository.getListCourseWithStudent(courseID);
    }
    public Course getCoursebyName(String course_name)
    {
        return courseRepository.findCourseByName(course_name);
    }
}
