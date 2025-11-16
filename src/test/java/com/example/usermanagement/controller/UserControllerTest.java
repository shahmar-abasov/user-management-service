package com.example.usermanagement.controller;

import com.example.usermanagement.dto.UserRequestDto;
import com.example.usermanagement.dto.UserResponseDto;
import com.example.usermanagement.dto.UserUpdateDto;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponseDto userResponseDto;
    private UserRequestDto userRequestDto;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userResponseDto = new UserResponseDto(1L, "Test Name", "test@mail.com", "+123456789", User.UserRole.USER, true,
                LocalDateTime.now(), LocalDateTime.now());
        userRequestDto = new UserRequestDto("Test Name", "test@mail.com", "+123456789", User.UserRole.USER, true);
        userUpdateDto = new UserUpdateDto("Updated Name", "updated@mail.com", "+198765432", User.UserRole.ADMIN, false);
    }

    @Test
    void healthCheck_ReturnsOk() {
        ResponseEntity<String> response = userController.healthCheck();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("User Management Service is running!");
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        when(userService.createUser(userRequestDto)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.createUser(userRequestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(userResponseDto);
        verify(userService, times(1)).createUser(userRequestDto);
    }

    @Test
    void getUserById_ReturnsUser() {
        when(userService.getUserById(1L)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.getUserById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userResponseDto);
        verify(userService).getUserById(1L);
    }

    @Test
    void getAllUsers_ReturnsUserList() {
        List<UserResponseDto> users = Arrays.asList(userResponseDto);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserResponseDto>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(users);
        verify(userService).getAllUsers();
    }

    @Test
    void updateUser_ReturnsUpdatedUser() {
        when(userService.updateUser(eq(1L), any(UserUpdateDto.class))).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.updateUser(1L, userUpdateDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userResponseDto);
        verify(userService, times(1)).updateUser(1L, userUpdateDto);
    }

    @Test
    void deleteUser_ReturnsNoContent() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userService, times(1)).deleteUser(1L);
    }
}