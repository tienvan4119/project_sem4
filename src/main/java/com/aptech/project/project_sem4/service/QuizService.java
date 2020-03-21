package com.aptech.project.project_sem4.service;

import com.aptech.project.project_sem4.model.Topic;
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

    public List<Topic> listAllTopicBySection_id(String section_id)
    {
        return topicRepository.findTopicsBySectionId(section_id);
    }
    public List<String> listRandomQuestion(String section_id)
    {
//        MongoClient mongoClient = new MongoClient("localhost", 27017);
//        MongoDatabase database = mongoClient.getDatabase("multipleChoice_Project");
//        MongoCollection<Document> collection = database.getCollection("question");
//        AggregateIterable<Document> documents = collection.aggregate(Arrays.asList(Aggregates.sample(3)));
//        List<String> lst = new LinkedList<>();
//        for(Document d: documents) {
//            lst.add(d.getString("_id"));
//        }
//        return  lst;
    }
}
