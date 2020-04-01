package com.aptech.project.project_sem4.model;

import groovy.transform.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
@ToString(excludes = {"id", "sectionId"})
@Document(collection = "topic")
public class Topic {
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ObjectId getSectionId() {
        return section_id;
    }

    public void setSectionId(ObjectId sectionId) {
        this.section_id = sectionId;
    }

    @Id
    ObjectId id;
    String title;
    ObjectId section_id;

}
