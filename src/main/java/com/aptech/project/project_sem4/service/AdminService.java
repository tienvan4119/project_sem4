package com.aptech.project.project_sem4.service;

import com.aptech.project.project_sem4.model.Role;
import com.aptech.project.project_sem4.model.User;
import com.aptech.project.project_sem4.repository.RoleRepository;
import com.aptech.project.project_sem4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service("adminService")
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    public User findUserbyId(String id)
    {
        return userRepository.findUserById(id);
    }

    public void saveUser(User user) {
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

}
