package com.aptech.project.project_sem4.configuration;

import com.aptech.project.project_sem4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Bean
    public UserDetailsService mongoUserDetails() {
        return new UserService();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService = mongoUserDetails();
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http)  throws Exception{
    http
            .authorizeRequests()
            .antMatchers("/","/login").permitAll()
            .antMatchers("/userProfile/**").hasAuthority("USER")
            .antMatchers("/userProfile.html/**").hasAuthority("USER")
            .antMatchers("/section/**").hasAuthority("USER")
            .antMatchers("/section.html/**").hasAuthority("USER")
            .antMatchers("/khoa/**").hasAuthority("ADMIN")
            .antMatchers("/khoa.html/**").hasAuthority("ADMIN")
            .antMatchers("/class/**").hasAuthority("TEACHER")
            .antMatchers("/class.html/**").hasAuthority("TEACHER")
            .antMatchers("/teacher/**").hasAuthority("TEACHER")
            .antMatchers("/teacher_OLD.html/**").hasAuthority("TEACHER")
            .antMatchers("/addQuiz/**").hasAuthority("TEACHER")
            .antMatchers("/addQuiz.html/**").hasAuthority("TEACHER")
            .antMatchers("/addBaiLam/**").hasAuthority("TEACHER")
            .antMatchers("/addBaiLam_Old.html/**").hasAuthority("TEACHER")
            .antMatchers("/updateQuiz/**").hasAuthority("TEACHER")
            .antMatchers("/updateQuiz.html/**").hasAuthority("TEACHER")
            .antMatchers("/form/**/").hasAuthority("ADMIN")
            .antMatchers("/form.html/**/").hasAuthority("ADMIN")
            .antMatchers("/admin/**").hasAuthority("ADMIN")
            .antMatchers("/admin.html/**").hasAuthority("ADMIN").anyRequest()
            .authenticated().and().csrf().disable().formLogin().successHandler(customizeAuthenticationSuccessHandler)
            .loginPage("/login").failureUrl("/login?error=true")
            .usernameParameter("email")
            .passwordParameter("password")
            .and().logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login").and().exceptionHandling();;

}
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**","/vendor/**");
    }

}
