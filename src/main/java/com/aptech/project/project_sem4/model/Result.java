package com.aptech.project.project_sem4.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(value = "result")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    ObjectId id;
    ObjectId user_id;
    ObjectId testId;
    ObjectId courseId;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getTestId() {
        return testId;
    }

    public void setTestId(ObjectId testId) {
        this.testId = testId;
    }

    public ObjectId getCourseId() {
        return courseId;
    }

    public void setCourseId(ObjectId courseId) {
        this.courseId = courseId;
    }

    public ObjectId getUser_id() {
        return user_id;
    }

    public void setUser_id(ObjectId user_id) {
        this.user_id = user_id;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    double mark;

    public boolean isDoneisDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean isTestAgain() {
        return testAgain;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTestAgain(boolean testAgain) {
        this.testAgain = testAgain;
    }

    boolean isDone;
    boolean testAgain;
    int time;

}
