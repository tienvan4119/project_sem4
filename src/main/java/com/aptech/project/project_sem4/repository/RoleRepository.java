/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aptech.project.project_sem4.repository;


import com.aptech.project.project_sem4.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;


public interface RoleRepository extends CrudRepository<Role, String> {
    Role findByRole(String role);
}
