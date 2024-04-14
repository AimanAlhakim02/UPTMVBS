/**
 * Repository interface class for 'User'
 */
package com.library.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Annotate as Repository class
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    // Annotate as query
    // We create a custom query to find users by their email
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);


}
