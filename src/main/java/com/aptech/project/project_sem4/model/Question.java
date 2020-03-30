package com.aptech.project.project_sem4.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(collection = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;
    String question_desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion_desc() {
        return question_desc;
    }

    public void setQuestion_desc(String question_desc) {
        this.question_desc = question_desc;
    }

    public String getTopic_id() {
        return topicId;
    }

    public void setTopic_id(String topic_id) {
        this.topicId = topic_id;
    }

    String topicId;
}
