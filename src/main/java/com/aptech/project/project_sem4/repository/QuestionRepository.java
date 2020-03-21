package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository  extends MongoRepository<Question, Long> {
    @Query("{$sample: {size: 3} }")
    public List<Question> findQuestionByTopicId(String topic_id);
}
