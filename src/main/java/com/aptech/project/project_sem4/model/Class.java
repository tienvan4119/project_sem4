package com.aptech.project.project_sem4.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(collection = "class")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    ObjectId id;
    String className;
    ObjectId facultyId;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String class_Name) {
        this.className = class_Name;
    }

    public ObjectId getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(ObjectId faculty_id) {
        this.facultyId = faculty_id;
    }
}
