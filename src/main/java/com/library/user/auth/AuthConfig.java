/**
 * Configuration for Spring Framework Security
 * We use Spring Security for the account authentication mechanisms
 * It has neat features like password encryption and role-based access
 */
package com.library.user.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Annotate as configuration
@Configuration
// Marker annotation to help Spring find the class
@EnableWebSecurity

public class AuthConfig extends WebSecurityConfigurerAdapter {

    // Annotate as Bean
    @Bean
    public UserDetailsService userDetailsService() {
        // Return our instance of UserDetailsService, MyUserDetailsService
        return new MyUserDetailsService();
    }

    // Annotate as Bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Return instance of BCryptPasswordEncoder...
        // for password encryption with BCrypt
        return new BCryptPasswordEncoder();
    }

    // Annotate as Bean
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Define new instance of DaoAuthenticationProvider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Set userDetailsService as the UserDetailsService
        authProvider.setUserDetailsService(userDetailsService());
        // Set passwordEncoder as the password encoder
        authProvider.setPasswordEncoder(passwordEncoder());

        // Return the instance
        return authProvider;
    }

    // Annotate as override method
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set authentication provider
        auth.authenticationProvider(authenticationProvider());
        // Additionally, we create a user account to be inserted into the RAM...
        // at runtime.
        // Specify the password, then encode it
        String password = passwordEncoder().encode("uptm123");
        // To be inserted in memory
        auth.inMemoryAuthentication()
                // Create user admin@library.com with the encoded password...
                // and give the user ADMIN role
                .withUser("admin@uptm.edu.my").password(password).roles("ADMIN");
    }

    // Annotate as override method
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure http security
        http.authorizeRequests()
                // URL matching "/student" only accessible if authenticated/logged in
                .antMatchers("/student").authenticated()
                // URL matching "/admin/**/" only accessible if has ADMIN role
                // Wildcard ** indicates all subsequent URLs following "/admin/ path is inclusive
                .antMatchers("/admin/**/").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                // Configure the login form/page
                .formLogin()
                // Return templates/login.html
                .loginPage("/login")
                // Specify we take the "email" element as username
                .usernameParameter("email")
                // Upon successful login, redirect to "/"
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                // Configure the logout mechanism
                .logout()
                // Redirect to "/logout" after logging out, and clear cookies and...
                // http session
                .logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                // Upon successful logout, redirect to "/login?logout". Param logout...
                // is to implement a status message
                .logoutSuccessUrl("/login?logout").permitAll();
    }

}
