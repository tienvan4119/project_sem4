package com.aptech.project.project_sem4.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Document(collection = "section")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    ObjectId id;
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Field(value = "title")
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public ObjectId getFaculty_id() {
        return faculty_id;
    }

    public void setFaculty_id(ObjectId faculty_id) {
        this.faculty_id = faculty_id;
    }

    private List<Topic> topics;
    ObjectId faculty_id;
}
