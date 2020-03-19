package com.aptech.project.project_sem4.service;

import com.aptech.project.project_sem4.model.Topic;
import com.aptech.project.project_sem4.repository.TopicRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    @Autowired
    private TopicRepository topicRepository;
    public List<Topic> listAllTopicBySection_id(String section_id)
    {
        return topicRepository.findTopicsBySectionId(section_id);
    }
}
