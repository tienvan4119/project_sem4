package com.aptech.project.project_sem4.repository;

import com.aptech.project.project_sem4.model.RelationCourseSection;
import com.aptech.project.project_sem4.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationCourseSectionRepository extends MongoRepository<RelationCourseSection, Long> {
    @Query("{ courseId: ObjectId(\"?0\" )}")
    public RelationCourseSection findbyCourseId(String course_id);
}
