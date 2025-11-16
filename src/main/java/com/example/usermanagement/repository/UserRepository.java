package com.example.usermanagement.repository;

import com.example.usermanagement.model.User;
import com.example.usermanagement.model.User.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	Page<User> findByActiveTrue(Pageable pageable);

	Page<User> findByRole(UserRole role, Pageable pageable);

	Page<User> findByActiveAndRole(Boolean active, UserRole role, Pageable pageable);
}