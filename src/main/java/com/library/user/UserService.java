/**
 * Service class & business logics for 'User'
 */
package com.library.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Annotate as Service class
@Service
public class UserService {

    // Inject instance of UserRepository as repo
    @Autowired
    private UserRepository repo;

    // Define list of users to be used in controllers
    public List<User> listAll() {
        // Return all entries as List, use findAll() from CrudRepository
        return (List<User>) repo.findAll();
    }

    // Define user saving to be used in controllers
    public void save(User user) {

        // Save user
        repo.save(user);
    }


}
