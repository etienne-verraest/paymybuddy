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

import java.util.ArrayList;
import java.util.List;

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
	static User mockBuddy;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		mockUser = new User();
		mockUser.setId(1);
		mockUser.setFirstName("Floyd");
		mockUser.setLastName("Lilly");
		mockUser.setMail("f.lilly@localhost.fr");
		mockUser.setPassword(new BCryptPasswordEncoder().encode("pass"));
		mockUser.setBalance(10.0);

		mockBuddy = new User();
		mockBuddy.setId(2);
		mockBuddy.setFirstName("Ronald");
		mockBuddy.setLastName("Carlson");
		mockBuddy.setMail("r.carlson@localhost.fr");
		mockBuddy.setPassword(new BCryptPasswordEncoder().encode("password"));
	}

	@Test
	void testFindUserById_ShouldReturn_MockUser() {

		// ARRANGE
		when(userRepositoryMock.findUserById(anyInt())).thenReturn(mockUser);

		// ACT
		User response = userService.findUserById(1);

		// ASSERT
		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getMail()).isEqualTo("f.lilly@localhost.fr");
		assertThat(response.getFirstName()).isEqualTo("Floyd");
	}

	@Test
	void testFindUserByMail_ShouldReturn_MockUser() {

		// ARRANGE
		when(userRepositoryMock.findUserByMail(anyString())).thenReturn(mockUser);

		// ACT
		User response = userService.findUserByMail("f.lilly@localhost.fr");

		// ASSERT
		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getMail()).isEqualTo("f.lilly@localhost.fr");
		assertThat(response.getLastName()).isEqualTo("Lilly");
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
		UserDetails details = userService.loadUserByUsername("f.lilly@localhost.fr");

		// ASSERT
		assertThat(details.getUsername()).isEqualTo("f.lilly@localhost.fr");
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

	@Test
	void testGetListOfUserFromIdentifiers_ShouldReturn_ListOfUser() {

		// ARRANGE
		List<Integer> identifiers = new ArrayList<>();
		identifiers.add(1);
		identifiers.add(2);
		when(userRepositoryMock.findUserById(1)).thenReturn(mockUser);
		when(userRepositoryMock.findUserById(2)).thenReturn(mockBuddy);

		// ACT
		List<User> response = userService.getListOfUserFromIdentifiers(identifiers);

		// ASSERT
		assertThat(response).hasSize(2);
		assertThat(response.get(0).getMail()).isEqualTo("f.lilly@localhost.fr");
		assertThat(response.get(1).getMail()).isEqualTo("r.carlson@localhost.fr");
	}
}
