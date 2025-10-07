package com.hung.expensive.repository;

import com.hung.expensive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Why: Repository interface cho User entity
 * Design decision: Extend JpaRepository để có sẵn CRUD operations
 * Business rule: Tìm user theo username hoặc email để check duplicate
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Why: Tìm user theo username - Spring Data JPA tự động implement
     * Context: Dùng cho authentication và check duplicate username
     * @param username username cần tìm
     * @return Optional<User> để tránh NullPointerException
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Why: Tìm user theo email - Spring Data JPA tự động implement
     * Context: Dùng cho authentication và check duplicate email
     * @param email email cần tìm
     * @return Optional<User> để tránh NullPointerException
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Why: Check xem username đã tồn tại chưa
     * Context: Validation trước khi tạo user mới
     * @param username username cần check
     * @return true nếu đã tồn tại
     */
    boolean existsByUsername(String username);
    
    /**
     * Why: Check xem email đã tồn tại chưa
     * Context: Validation trước khi tạo user mới
     * @param email email cần check
     * @return true nếu đã tồn tại
     */
    boolean existsByEmail(String email);
}

