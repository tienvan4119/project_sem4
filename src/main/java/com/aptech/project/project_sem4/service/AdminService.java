package com.aptech.project.project_sem4.service;

import com.aptech.project.project_sem4.model.*;
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
    public User findUserbyId(String id)
    {
        return userRepository.findUserById(id);
    }

    public void saveAdmin(User user) {
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
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
}
