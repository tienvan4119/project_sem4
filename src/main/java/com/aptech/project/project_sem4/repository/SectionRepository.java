package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends MongoRepository<Section, Long> {
}
