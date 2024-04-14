/**
 * Class MyUserDetails
 * For use by Spring Security to retrieve user details
 */
package com.library.user.auth;

import java.util.Collection;

import com.library.user.User;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {

    // Instantiate a new User object
    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    // Override method
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Do nothing.
        // We don't implement GrantedAuthority to any accounts
        return null;
    }

    // Override method
    // Get password of user
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Override method
    // Get username of user
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // Get Id of user
    public Integer getId() {
        return user.getId();
    }

    // Override method
    // Specify that user account is not expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Override method
    // Specify that user account is not locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Override method
    // Specify that user account's credentials is not expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Override method
    // Specify that user account is enabled
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Get the full name of users by combining getFirstName() and...
    // getLastName() from user object
    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

}