package com.aptech.project.project_sem4.service;

import com.aptech.project.project_sem4.model.Question;
import com.aptech.project.project_sem4.model.Section;
import com.aptech.project.project_sem4.model.Topic;
import com.aptech.project.project_sem4.repository.QuestionRepository;
import com.aptech.project.project_sem4.repository.TopicRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class QuizService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    MongoTemplate mongoTemplate = null;
    @Autowired
    private QuestionRepository questionRepository;

    public List<Topic> listAllTopicBySection_id(String section_id)
    {
        return topicRepository.findTopicsBySectionId(section_id);
    }
    public List<Question> listAllQuestion()
    {
        return questionRepository.findAll();
    }

}
