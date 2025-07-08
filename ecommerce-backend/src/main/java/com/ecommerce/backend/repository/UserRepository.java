package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by mobile number
     */
    Optional<User> findByMobileNumber(String mobileNumber);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by username or mobile number (for login)
     */
    @Query("SELECT u FROM User u WHERE u.username = :usernameOrMobile OR u.mobileNumber = :usernameOrMobile")
    Optional<User> findByUsernameOrMobileNumber(@Param("usernameOrMobile") String usernameOrMobile);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if mobile number exists
     */
    boolean existsByMobileNumber(String mobileNumber);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find active users only
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    Iterable<User> findAllActive();
    
    /**
     * Find user by username and check if active
     */
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.isActive = true")
    Optional<User> findByUsernameAndActive(@Param("username") String username);
    
    /**
     * Find user by mobile number and check if active
     */
    @Query("SELECT u FROM User u WHERE u.mobileNumber = :mobileNumber AND u.isActive = true")
    Optional<User> findByMobileNumberAndActive(@Param("mobileNumber") String mobileNumber);
} 