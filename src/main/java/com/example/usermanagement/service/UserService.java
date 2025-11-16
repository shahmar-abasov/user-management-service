package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserRequestDto;
import com.example.usermanagement.dto.UserResponseDto;
import com.example.usermanagement.dto.UserUpdateDto;
import com.example.usermanagement.exception.DuplicateResourceException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

	private final UserRepository userRepository;

	public UserResponseDto createUser(UserRequestDto requestDto) {
		log.info("Creating new user with email: {}", requestDto.getEmail());

		if (userRepository.existsByEmail(requestDto.getEmail())) {
			log.warn("User with email {} already exists", requestDto.getEmail());
			throw new DuplicateResourceException("User with email " + requestDto.getEmail() + " already exists");
		}

		User user = mapToEntity(requestDto);
		User savedUser = userRepository.save(user);

		log.info("User created successfully with ID: {}", savedUser.getId());
		return mapToDto(savedUser);
	}

	@Transactional(readOnly = true)
	public UserResponseDto getUserById(Long id) {
		log.info("Fetching user with ID: {}", id);

		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

		return mapToDto(user);
	}

	@Transactional(readOnly = true)
	public List<UserResponseDto> getAllUsers() {
		log.info("Fetching all users");

		List<User> users = userRepository.findAll();
		return users.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<UserResponseDto> getAllUsers(int page, int size, String sortBy, String sortDirection) {
		log.info("Fetching users - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, sortDirection);

		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		Page<User> usersPage = userRepository.findAll(pageable);
		return usersPage.map(this::mapToDto);
	}

	@Transactional(readOnly = true)
	public Page<UserResponseDto> getUsersWithFilter(Boolean active, User.UserRole role, int page, int size,
			String sortBy, String sortDirection) {
		log.info("Fetching users with filter - active: {}, role: {}", active, role);

		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		Page<User> usersPage;

		if (active != null && role != null) {
			usersPage = userRepository.findByActiveAndRole(active, role, pageable);
		} else if (active != null) {
			usersPage = active ? userRepository.findByActiveTrue(pageable) : userRepository.findAll(pageable);
		} else if (role != null) {
			usersPage = userRepository.findByRole(role, pageable);
		} else {
			usersPage = userRepository.findAll(pageable);
		}

		return usersPage.map(this::mapToDto);
	}

	public UserResponseDto updateUser(Long id, UserUpdateDto updateDto) {
		log.info("Updating user with ID: {}", id);

		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

		if (updateDto.getEmail() != null && !updateDto.getEmail().equals(user.getEmail())) {
			if (userRepository.existsByEmail(updateDto.getEmail())) {
				throw new DuplicateResourceException("User with email " + updateDto.getEmail() + " already exists");
			}
			user.setEmail(updateDto.getEmail());
		}

		if (updateDto.getName() != null) {
			user.setName(updateDto.getName());
		}
		if (updateDto.getPhone() != null) {
			user.setPhone(updateDto.getPhone());
		}
		if (updateDto.getRole() != null) {
			user.setRole(updateDto.getRole());
		}
		if (updateDto.getActive() != null) {
			user.setActive(updateDto.getActive());
		}

		User updatedUser = userRepository.save(user);
		log.info("User updated successfully with ID: {}", updatedUser.getId());

		return mapToDto(updatedUser);
	}

	public void deleteUser(Long id) {
		log.info("Deleting user with ID: {}", id);

		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found with ID: " + id);
		}

		userRepository.deleteById(id);
		log.info("User deleted successfully with ID: {}", id);
	}

	// Helper methods
	private User mapToEntity(UserRequestDto dto) {
		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPhone(dto.getPhone());
		user.setRole(dto.getRole() != null ? dto.getRole() : User.UserRole.USER);
		user.setActive(dto.getActive() != null ? dto.getActive() : true);
		return user;
	}

	private UserResponseDto mapToDto(User user) {
		return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole(),
				user.getActive(), user.getCreatedAt(), user.getUpdatedAt());
	}
}