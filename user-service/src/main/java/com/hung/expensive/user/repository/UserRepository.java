package com.hung.expensive.user.repository;

import com.hung.expensive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Why: User Repository cho database operations trong User Service
 * Design decision: JPA Repository pattern để simplify database access
 * Business requirement: Cần query user by username, email, và các operations khác
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Why: Find user by username - Spring Data JPA tự động implement
     * Business rule: Username phải unique trong hệ thống
     */
    Optional<User> findByUsername(String username);

    /**
     * Why: Find user by email - Spring Data JPA tự động implement
     * Business rule: Email phải unique trong hệ thống
     */
    Optional<User> findByEmail(String email);

    /**
     * Why: Check if email exists - custom query cho performance
     * Business rule: Cần check duplicate email khi update profile
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    /**
     * Why: Check if username exists - custom query cho performance
     * Business rule: Cần check duplicate username khi update profile
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);

    /**
     * Why: Find enabled users only - security consideration
     * Business rule: Chỉ return enabled users
     */
    @Query("SELECT u FROM User u WHERE u.enabled = true AND u.id = :userId")
    Optional<User> findEnabledUserById(@Param("userId") Long userId);

    /**
     * Why: Find enabled user by username - security consideration
     * Business rule: Chỉ return enabled users
     */
    @Query("SELECT u FROM User u WHERE u.enabled = true AND u.username = :username")
    Optional<User> findEnabledUserByUsername(@Param("username") String username);
}
