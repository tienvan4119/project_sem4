package com.aptech.project.project_sem4.service;

import com.aptech.project.project_sem4.model.*;
import com.aptech.project.project_sem4.model.Class;
import com.aptech.project.project_sem4.repository.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private ClassRepository classRepository;
@Autowired
private ResultRepository resultRepository;
@Autowired
private  RelationCourseSectionRepository relationCourseSectionRepository;
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Class findClassByID(String classID)
    {
        return classRepository.findClassById(classID);
    }

    public RelationCourseSection getCourseSection(String course_id)
    {
        return relationCourseSectionRepository.findbyCourseId(course_id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public void saveUser(User user) {
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
    public List<User> listAll()
    {
        return userRepository.findAll();
    }
    public List<Role> listAllRole()
    {
        return roleRepository.findAll();
    }
    public List<Section> listAllSection()
    {
        return sectionRepository.findAll();
    }
    public List<Result> getListSection_Result(String user_id)
    {
        return resultRepository.getListSection_Result(user_id);
    }
    public Result getResult(String user_id, String testId)
    {
        return resultRepository.getResult(user_id, testId);
    }
    public User saveUserProfile(User user)
    {
        return userRepository.save(user);
    }
    public void Delete_User(String user_id)
    {
        userRepository.delete(userRepository.findUserById(user_id));
    }
    public List<Result> getListResultofStudent(String course_id, String user_id)
    {
        return resultRepository.getListResultofStudent(course_id, user_id);
    }
}
