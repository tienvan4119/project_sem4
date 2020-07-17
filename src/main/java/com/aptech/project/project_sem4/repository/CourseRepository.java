package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.Course;
import com.aptech.project.project_sem4.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, Long> {
    @Query("{ teacherID: ObjectId(\"?0\" )}")
    public List<Course> findCoursesByTeacherID(String teacherID);
}
