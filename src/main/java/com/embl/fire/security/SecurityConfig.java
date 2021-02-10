package com.embl.fire.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}user@123").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin@123").roles("USER", "ADMIN");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/embl-fire/persons/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/embl-fire/persons**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/embl-fire/persons/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/embl-fire/persons/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}
