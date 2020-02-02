package com.aptech.project.project_sem4;

import com.aptech.project.project_sem4.model.Role;
import com.aptech.project.project_sem4.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class ProjectSem4Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectSem4Application.class, args);
    }


}
