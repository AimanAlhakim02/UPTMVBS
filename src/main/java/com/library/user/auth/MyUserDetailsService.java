/**
 * Service class for 'MyUserDetails'
 * For use by Spring Security to retrieve user details
 */
package com.library.user.auth;

import com.library.user.User;
import com.library.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {

    // Inject instance of UserRepository as repo
    @Autowired
    private UserRepository repo;

    // Override method
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Call findByEmail UserRepository method
        User user = repo.findByEmail(username);
        // User not found
        if (user == null) {
            // Throw exception with message
            throw new UsernameNotFoundException("User not found");
        }
        // User found, return user details
        return new MyUserDetails(user);
    }

}
