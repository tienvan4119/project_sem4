package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.RelationStudentCourse;
import com.aptech.project.project_sem4.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationStudentCourseRepository extends MongoRepository<RelationStudentCourse, Long> {
    @Query("{ courseID: ObjectId(\"?0\" )}")
    public List<RelationStudentCourse> getListCourseWithStudent(String courseID);

    @Query("{ studentID: ObjectId(\"?0\" )}")
    public List<RelationStudentCourse> getListCourseByStudentID(String student_id);

    @Query("{ courseID: ObjectId(\"?0\" ), studentID: ObjectId(\"?1\" )}")
    public RelationStudentCourse getCourseWithStudent(String courseID, String student_id);

}
