package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.Section;
import com.aptech.project.project_sem4.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends MongoRepository<Test, Long> {
    @Query("{_id : ObjectId(\"?0\" )}")
    Test getTestById(String test_id);

    @Query("{teacherID : ObjectId(\"?0\" )}")
    List<Test> findAllTest(String teacher_id);

    @Query("{courseID : ObjectId(\"?0\" )}")
    List<Test> findTestByCourseID(String courseID);

    @Query("{_id : ObjectId(\"?0\" )}")
    List<Test> getListTestById(String test_id);

}
