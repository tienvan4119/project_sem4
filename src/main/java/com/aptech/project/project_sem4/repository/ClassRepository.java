package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.Choice;
import com.aptech.project.project_sem4.model.Class;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends MongoRepository<Class, Long> {
    @Query("{ _id : ObjectId(\"?0\" )}")
    public Class findClassById(String classID);
    @Query("{ facultyId : ObjectId(\"?0\" )}")
    public List<Class> findClassesByFacultyId(String facultyId);

    @Query("{ className : \"?0\" }")
    public Class findClassByClassName(String className);

}
