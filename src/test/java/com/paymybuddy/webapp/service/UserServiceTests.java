package com.paymybuddy.webapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.paymybuddy.webapp.exception.UserServiceException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepositoryMock;

	static User mockUser;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		mockUser = new User();
		mockUser.setId(1);
		mockUser.setFirstName("Alpha");
		mockUser.setLastName("Bravo");
		mockUser.setMail("test@localhost.fr");
		mockUser.setPassword(new BCryptPasswordEncoder().encode("pass"));
	}

	@Test
	void testFindUserById_ShouldReturn_MockUser() {

		// ARRANGE
		when(userRepositoryMock.findUserById(anyInt())).thenReturn(mockUser);

		// ACT
		User response = userService.findUserById(1);

		// ASSERT
		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getMail()).isEqualTo("test@localhost.fr");
		assertThat(response.getFirstName()).isEqualTo("Alpha");
	}

	@Test
	void testFindUserByMail_ShouldReturn_MockUser() {

		// ARRANGE
		when(userRepositoryMock.findUserByMail(anyString())).thenReturn(mockUser);

		// ACT
		User response = userService.findUserByMail("test@localhost.fr");

		// ASSERT
		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getMail()).isEqualTo("test@localhost.fr");
		assertThat(response.getLastName()).isEqualTo("Bravo");
	}

	@Test
	void testCreateUser_VerifyThat_RepositoryIsCalled() throws UserServiceException {

		// ARRANGE
		when(userRepositoryMock.save(any(User.class))).thenReturn(mockUser);

		// ACT AND ASSERT
		userService.createUser(mockUser);

		// ASSERT
		verify(userRepositoryMock, times(1)).save(mockUser);

	}

	@Test
	void testCreateNullUser_VerifyThat_RepositoryIsNotCalled() throws UserServiceException {

		// ARRANGE
		lenient().when(userRepositoryMock.save(any(User.class))).thenReturn(null);

		// ACT AND ASSERT
		userService.createUser(null);

		// ASSERT
		verify(userRepositoryMock, never()).save(null);

	}

	@Test
	void testLoadUserByUsername_ReturnNewUser() {

		// ARRANGE
		when(userRepositoryMock.findUserByMail(anyString())).thenReturn(mockUser);

		// ACT
		UserDetails details = userService.loadUserByUsername("test@localhost.fr");

		// ASSERT
		assertThat(details.getUsername()).isEqualTo("test@localhost.fr");
		assertThat(details.getAuthorities()).hasSize(1);
	}

	@Test
	void testLoadUserByUsername_ShouldThrow_UsernameNotFoundException() {

		// ARRANGE
		when(userRepositoryMock.findUserByMail(anyString())).thenReturn(null);

		// ACT
		Executable executable = () -> userService.loadUserByUsername("wrong@address.com");

		// ASSERT
		assertThrows(UsernameNotFoundException.class, executable);

	}
}
