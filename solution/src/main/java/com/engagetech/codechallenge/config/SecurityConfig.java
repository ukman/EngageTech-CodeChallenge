package com.engagetech.codechallenge.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security configurations
 */
@Configuration
@ComponentScan
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/**").authenticated()
                // .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    /**
     * Configuration of users
     * TODO redesign for real prod. Use OpenID/DB for keeping users.
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception
    {
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}password").roles("USER", "ADMIN")
                .and().withUser("user").password("{noop}password").roles("USER")
        ;
    }

    /**
     * Bean for providing current user info for @CreatedBy/@ModifiedBy annotations
     * @return
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new SecurityAuditorAware();
    }
}
