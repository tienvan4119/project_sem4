package com.aptech.project.project_sem4.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(collection = "choice")
public class Choice {
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(ObjectId question_id) {
        this.question_id = question_id;
    }

    public String getChoice_desc() {
        return choice_desc;
    }

    public void setChoice_desc(String choice_desc) {
        this.choice_desc = choice_desc;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    ObjectId id;
    ObjectId question_id;
    String choice_desc;
    Boolean isCorrect;
}
