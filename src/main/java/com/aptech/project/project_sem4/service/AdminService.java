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
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private RelationCourseSectionRepository relationCourseSectionRepository;
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

    public void saveTeacherEdit(User user)
    {
        userRepository.save(user);
    }

    public void saveQuizTest(Test test) {

        testRepository.save(test);
    }

    public void saveCourseSection(RelationCourseSection courseSection)
    {
        relationCourseSectionRepository.save(courseSection);
    }

    public List<Result> getListResultByTestId(String test_id)
    {
        return resultRepository.getListResultByTestId(test_id);
    }

    public void DeleteTeacher(User user)
    {
        userRepository.delete(user);
    }

    public List<Test> getListTestOfCourse(String courseID)
    {
        return testRepository.findTestByCourseID(courseID);
    }

    public List<Result> getFullResultofCourse(String course_id)
    {
        return resultRepository.getListResultofCourse(course_id);
    }

    public Result getResultbyTestAndUser(String test_id, String user_id)
    {
        return resultRepository.getResultByTestAndUser(test_id, user_id);
    }

    public void DeleteQuestion(Question question){ questionRepository.delete(question);}

    public void DeleteTest(String test_id)
    {
        testRepository.delete(testRepository.getTestById(test_id));
    }

    public Test getTestbyId(String test_id)
    {
        return testRepository.getTestById(test_id);
    }

    public List<Test> getListTest(String teacherID)
    {
        return testRepository.findAllTest(teacherID);
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
    public  List<Class> getAllClass(){ return classRepository.findAll();}
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
    public void saveStudentCourse(RelationStudentCourse studentCourse)
    {
        relationStudentCourseRepository.save(studentCourse);
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

    public List<Test> getListTestByID(String test_id)
    {
        return testRepository.getListTestById(test_id);
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
    public void saveSection(Section section) {
        sectionRepository.save(section);
    }
    public void saveTopic(Topic topic) {
        topicRepository.save(topic);
    }

    public void saveStudentEdit(User user)
    {
        userRepository.save(user);
    }
    public void DeleteStudent(User user)
    {
        userRepository.delete(user);
    }
    public void DeleteRelationCourseStudent(RelationStudentCourse studentCourse)
    {
        relationStudentCourseRepository.delete(studentCourse);
    }
    public RelationStudentCourse getRelationCourseStudent(String course_id, String student_id)
    {
        return relationStudentCourseRepository.getCourseWithStudent(course_id,student_id);
    }

    public List<RelationStudentCourse> getListCourseofStudent(String student_id)
    {
        return relationStudentCourseRepository.getListCourseByStudentID(student_id);
    }
    public Class findClassByName(String className)
    {
        return classRepository.findClassByClassName(className);
    }
    public void DeleteChoice(Choice choice){ choiceRepository.delete(choice);}
    public List<Choice> findChoiceByQuestionId(String question_id){ return choiceRepository.findChoiceByQuestionId(question_id);}
    public Question findQuestionByDesc(String question_desc){ return questionRepository.findQuestionByDesc(question_desc);}
    public void saveAdminProfile(User user)
    {
        userRepository.save(user);
    }
    public List<Course> getFullCourse()
    {
        return courseRepository.findAll();
    }
    public void saveNewCourse(Course course)
    {
        courseRepository.save(course);
    }
    public void DeleteStudentOfCourse(RelationStudentCourse studentCourse)
    {
        relationStudentCourseRepository.delete(studentCourse);
    }
    public void DeleteCourse(Course course)
    {
        courseRepository.delete(course);
    }
    public void SaveCourseSection(RelationCourseSection courseSection)
    {
        relationCourseSectionRepository.save(courseSection);
    }
    public void DeleteResult(Result result)
    {
        resultRepository.delete(result);
    }
    public Question getQuestionById(String question_id)
    {
        return questionRepository.findTopicByQuestionId(question_id);
    }
    public RelationCourseSection findbyCourseId(String course_id)
    {
        return relationCourseSectionRepository.findbyCourseId(course_id);
    }
    public void DeleteCourseSection(RelationCourseSection courseSection)
    {
        relationCourseSectionRepository.delete(courseSection);
    }
}
