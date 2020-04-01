package com.aptech.project.project_sem4.service;

import com.aptech.project.project_sem4.model.Choice;
import com.aptech.project.project_sem4.model.Question;
import com.aptech.project.project_sem4.model.Topic;
import com.aptech.project.project_sem4.repository.ChoiceRepository;
import com.aptech.project.project_sem4.repository.QuestionRepository;
import com.aptech.project.project_sem4.repository.TopicRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    MongoTemplate mongoTemplate = null;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChoiceRepository choiceRepository;

    public List<Topic> listAllTopicBySection_id(String section_id)
    {
        return topicRepository.findTopicsBySectionId(section_id);
    }
    public List<Question> listAllQuestion()
    {
        return questionRepository.findAll();
    }
    public List<Choice> listChoiceByQuestion_id(String question_id)
    {
       return choiceRepository.findChoiceByQuestionId(question_id);
    }
    public List<Question> listQuestionByTopic(String topic_id)
    {
        return questionRepository.findQuestionByTopicId(topic_id);
    }
}
