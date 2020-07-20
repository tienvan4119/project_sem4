package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends MongoRepository<Test, Long> {
}
