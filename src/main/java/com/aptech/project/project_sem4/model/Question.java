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
    ObjectId id;
    String question_desc;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getQuestion_desc() {
        return question_desc;
    }

    public void setQuestion_desc(String question_desc) {
        this.question_desc = question_desc;
    }

    public ObjectId getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(ObjectId topic_id) {
        this.topic_id = topic_id;
    }

    ObjectId topic_id;
}
