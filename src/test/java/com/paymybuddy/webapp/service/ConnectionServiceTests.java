package com.paymybuddy.webapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.paymybuddy.webapp.exception.ConnectionServiceException;
import com.paymybuddy.webapp.model.Connection;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.ConnectionRepository;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTests {

	@InjectMocks
	ConnectionService connectionService;

	@Mock
	ConnectionRepository connectionRepositoryMock;

	@Mock
	UserService userService;

	static User mockUser;
	static User mockBuddy;
	static Connection mockConnection;
	static List<Connection> mockConnections = new ArrayList<>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		mockUser = new User();
		mockUser.setId(1);
		mockUser.setFirstName("Mark");
		mockUser.setLastName("Martinez");
		mockUser.setMail("m.martinez@localhost.fr");
		mockUser.setPassword(new BCryptPasswordEncoder().encode("password"));

		mockBuddy = new User();
		mockBuddy.setId(2);
		mockBuddy.setFirstName("Peter");
		mockBuddy.setLastName("Lamkin");
		mockBuddy.setMail("p.lamkin@localhost.fr");
		mockBuddy.setPassword(new BCryptPasswordEncoder().encode("password"));

		mockConnection = new Connection(mockUser.getId(), mockBuddy.getId());
		mockConnections.add(mockConnection);
	}

	@Test
	void testMakeConnections_ShouldReturn_NewConnection() throws ConnectionServiceException {

		// ARRANGE
		when(userService.isAnExistingMail(anyString())).thenReturn(true);
		when(userService.findUserByMail(anyString())).thenReturn(mockBuddy);
		when(connectionRepositoryMock.save(any(Connection.class))).thenReturn(mockConnection);

		// ACT
		boolean response = connectionService.makeConnections(mockUser.getId(), mockBuddy.getMail());

		// ASSERT
		assertThat(response).isTrue();
		verify(connectionRepositoryMock, times(1)).save(mockConnection);
	}

	@Test
	void testMakeConnections_ShouldReturn_ExceptionNoMailFound() {

		// ARRANGE
		when(userService.isAnExistingMail(anyString())).thenReturn(false);

		// ACT
		Executable executable = () -> connectionService.makeConnections(mockUser.getId(), mockBuddy.getMail());

		// ASSERT
		assertThrows(ConnectionServiceException.class, executable);
	}

	@Test
	void testMakeCOnnections_ShouldReturn_AlreadyFriend() {

		// ARRANGE
		when(userService.isAnExistingMail(anyString())).thenReturn(true);
		when(userService.findUserByMail(anyString())).thenReturn(mockBuddy);
		when(connectionRepositoryMock.findAllConnectionByUserId(anyInt())).thenReturn(mockConnections);

		// ACT
		Executable executable = () -> connectionService.makeConnections(mockUser.getId(), mockBuddy.getMail());

		// ASSERT
		assertThrows(ConnectionServiceException.class, executable);
	}

	@Test
	void testMakeCOnnections_ShouldReturn_UserTriesToAddHimself() {

		// ARRANGE
		when(userService.isAnExistingMail(anyString())).thenReturn(true);
		when(userService.findUserByMail(anyString())).thenReturn(mockUser);

		// ACT
		Executable executable = () -> connectionService.makeConnections(mockUser.getId(), mockUser.getMail());

		// ASSERT
		assertThrows(ConnectionServiceException.class, executable);
	}

	@Test
	void testRemoveConnections_ShouldReturn_True() {

		// ARRANGE
		when(connectionRepositoryMock.findAllConnectionByUserId(anyInt())).thenReturn(mockConnections);
		Integer userId = mockUser.getId();
		Integer buddyId = mockBuddy.getId();

		// ACT
		boolean response = connectionService.removeConnections(userId, buddyId);

		// ASSERT
		assertThat(response).isTrue();
		verify(connectionRepositoryMock, times(1)).deleteBuddyFromId(userId, buddyId);
	}

	@Test
	void testRemoveConnections_ShouldReturn_False() {

		// ARRANGE
		when(connectionRepositoryMock.findAllConnectionByUserId(anyInt())).thenReturn(new ArrayList<>());
		Integer userId = mockUser.getId();
		Integer buddyId = mockBuddy.getId();

		// ACT
		boolean response = connectionService.removeConnections(userId, buddyId);

		// ASSERT
		assertThat(response).isFalse();
		verify(connectionRepositoryMock, never()).deleteBuddyFromId(userId, buddyId);
	}
}
