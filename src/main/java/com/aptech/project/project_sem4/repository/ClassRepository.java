package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.Choice;
import com.aptech.project.project_sem4.model.Class;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends MongoRepository<Class, Long> {
    @Query("{ _id : ObjectId(\"?0\" )}")
    public Class findClasByID(String classID);

}
