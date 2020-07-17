package com.aptech.project.project_sem4.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(collection = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    ObjectId id;
    String name;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public ObjectId getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(ObjectId teacherID) {
        this.teacherID = teacherID;
    }

    String sub_name;
    ObjectId teacherID;
}
