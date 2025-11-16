package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserRequestDto;
import com.example.usermanagement.dto.UserResponseDto;
import com.example.usermanagement.dto.UserUpdateDto;
import com.example.usermanagement.exception.DuplicateResourceException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.model.User;
import com.example.usermanagement.model.User.UserRole;
import com.example.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private User testUser;
	private UserRequestDto requestDto;

	@BeforeEach
	void setUp() {
		// Test data
		testUser = new User();
		testUser.setId(1L);
		testUser.setName("Test User");
		testUser.setEmail("test@example.com");
		testUser.setPhone("+994501234567");
		testUser.setRole(UserRole.USER);
		testUser.setActive(true);
		testUser.setCreatedAt(LocalDateTime.now());
		testUser.setUpdatedAt(LocalDateTime.now());

		requestDto = new UserRequestDto();
		requestDto.setName("Test User");
		requestDto.setEmail("test@example.com");
		requestDto.setPhone("+994501234567");
		requestDto.setRole(UserRole.USER);
	}

	@Test
	void createUser_Success() {

		when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		UserResponseDto result = userService.createUser(requestDto);

		assertNotNull(result);
		assertEquals("Test User", result.getName());
		assertEquals("test@example.com", result.getEmail());
		verify(userRepository, times(1)).existsByEmail(requestDto.getEmail());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void createUser_DuplicateEmail_ThrowsException() {

		when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

		assertThrows(DuplicateResourceException.class, () -> {
			userService.createUser(requestDto);
		});
		verify(userRepository, times(1)).existsByEmail(requestDto.getEmail());
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void getUserById_Success() {

		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

		UserResponseDto result = userService.getUserById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Test User", result.getName());
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	void getUserById_NotFound_ThrowsException() {

		when(userRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			userService.getUserById(999L);
		});
		verify(userRepository, times(1)).findById(999L);
	}

	@Test
	void getAllUsers_Success() {

		User user2 = new User();
		user2.setId(2L);
		user2.setName("User 2");
		user2.setEmail("user2@example.com");

		when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

		List<UserResponseDto> result = userService.getAllUsers();

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(userRepository, times(1)).findAll();
	}

	@Test
	void updateUser_Success() {

		UserUpdateDto updateDto = new UserUpdateDto();
		updateDto.setName("Updated Name");
		updateDto.setPhone("+994559876543");

		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		UserResponseDto result = userService.updateUser(1L, updateDto);

		assertNotNull(result);
		verify(userRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void updateUser_DuplicateEmail_ThrowsException() {

		UserUpdateDto updateDto = new UserUpdateDto();
		updateDto.setEmail("existing@example.com");

		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

		assertThrows(DuplicateResourceException.class, () -> {
			userService.updateUser(1L, updateDto);
		});
		verify(userRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).existsByEmail("existing@example.com");
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void deleteUser_Success() {

		when(userRepository.existsById(1L)).thenReturn(true);
		doNothing().when(userRepository).deleteById(1L);

		userService.deleteUser(1L);

		verify(userRepository, times(1)).existsById(1L);
		verify(userRepository, times(1)).deleteById(1L);
	}

	@Test
	void deleteUser_NotFound_ThrowsException() {

		when(userRepository.existsById(999L)).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> {
			userService.deleteUser(999L);
		});
		verify(userRepository, times(1)).existsById(999L);
		verify(userRepository, never()).deleteById(anyLong());
	}
}