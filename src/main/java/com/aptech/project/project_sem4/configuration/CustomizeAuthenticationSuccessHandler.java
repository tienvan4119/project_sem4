/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aptech.project.project_sem4.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        //set our response to OK status
        response.setStatus(HttpServletResponse.SC_OK);

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            System.out.println("No");
            if ("ADMIN".equals(auth.getAuthority())) {
                response.sendRedirect("/admin");
            }
            else if ("USER".equals(auth.getAuthority())){
                response.sendRedirect("/userProfile");
            }
            else if ("TEACHER".equals(auth.getAuthority())){
                response.sendRedirect("/class");
            }
           /* if("USER".equals(auth.getAuthority())){
                response.sendRedirect("/index");
            }*/
        }
    }

}
