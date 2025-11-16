package com.example.usermanagement.controller;

import com.example.usermanagement.dto.UserRequestDto;
import com.example.usermanagement.dto.UserResponseDto;
import com.example.usermanagement.dto.UserUpdateDto;
import com.example.usermanagement.model.User.UserRole;
import com.example.usermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;

	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("User Management Service is running!");
	}

	@PostMapping
	public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
		log.info("POST /api/v1/users - Creating user with email: {}", requestDto.getEmail());
		UserResponseDto createdUser = userService.createUser(requestDto);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
		log.info("GET /api/v1/users/{} - Fetching user", id);
		UserResponseDto user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@GetMapping
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		log.info("GET /api/v1/users - Fetching all users");
		List<UserResponseDto> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/paginated")
	public ResponseEntity<Page<UserResponseDto>> getAllUsersPaginated(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {
		log.info("GET /api/v1/users/paginated - page: {}, size: {}", page, size);
		Page<UserResponseDto> users = userService.getAllUsers(page, size, sortBy, sortDirection);
		return ResponseEntity.ok(users);
	}

	@GetMapping("/filter")
	public ResponseEntity<Page<UserResponseDto>> getUsersWithFilter(@RequestParam(required = false) Boolean active,
			@RequestParam(required = false) UserRole role, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {
		log.info("GET /api/v1/users/filter - active: {}, role: {}", active, role);
		Page<UserResponseDto> users = userService.getUsersWithFilter(active, role, page, size, sortBy, sortDirection);
		return ResponseEntity.ok(users);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
			@Valid @RequestBody UserUpdateDto updateDto) {
		log.info("PUT /api/v1/users/{} - Updating user", id);
		UserResponseDto updatedUser = userService.updateUser(id, updateDto);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		log.info("DELETE /api/v1/users/{} - Deleting user", id);
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}