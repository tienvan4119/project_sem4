package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends MongoRepository<Result, Long> {
    @Query("{ user_id: ObjectId(\"?0\" )}")
    public List<Result> getListSection_Result(String User_id);

    @Query("{ user_id: ObjectId(\"?0\" ), testId: ObjectId(\"?1\" )}")
    public Result getResult(String user_id, String testId);

    @Query("{ courseId: ObjectId(\"?0\" )}")
    public List<Result> getListResultofCourse(String course_id);

    @Query("{ courseId: ObjectId(\"?0\" ),  user_id: ObjectId(\"?1\" )}")
    public List<Result> getListResultofStudent(String course_id, String user_id);

    @Query("{ testId: ObjectId(\"?0\" ),  user_id: ObjectId(\"?1\" )}")
    public Result getResultByTestAndUser(String test_id, String user_id);
    @Query("{ testId: ObjectId(\"?0\" )}")
    public List<Result> getListResultByTestId(String testId);
}
