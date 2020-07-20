package com.aptech.project.project_sem4.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(collection = "RelationStudentCourse")
public class RelationStudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    ObjectId id;
    ObjectId courseID;
    ObjectId studentID;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getCourseID() {
        return courseID;
    }

    public void setCourseID(ObjectId courseID) {
        this.courseID = courseID;
    }

    public ObjectId getStudentID() {
        return studentID;
    }

    public void setStudentID(ObjectId studentID) {
        this.studentID = studentID;
    }
}
