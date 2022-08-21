package com.paymybuddy.webapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import com.paymybuddy.webapp.exception.BankAccountServiceException;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.dto.BankAccountAddDto;
import com.paymybuddy.webapp.repository.BankAccountRepository;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTests {

	@InjectMocks
	BankAccountService bankAccountService;

	@Mock
	BankAccountRepository bankAccountRepositoryMock;

	@Mock
	UserService userService;

	static User mockUser;
	static BankAccount mockBankAccount;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		mockUser = new User();
		mockUser.setId(1);
		mockUser.setFirstName("Richard");
		mockUser.setLastName("Kimball");
		mockUser.setMail("r.kimball@localhost.fr");

		mockBankAccount = new BankAccount();
		mockBankAccount.setUser(mockUser);
		mockBankAccount.setBankName("European Bank");
		mockBankAccount.setIban("EU765504515816798265");
		mockBankAccount.setRib("76550451581679826512235");

		mockUser.setBankAccount(mockBankAccount);
	}

	@Test
	void testGetBankAccountInformation_ShouldReturn_BankAccount() {

		// ARRANGE
		when(bankAccountRepositoryMock.findByUserId(anyInt())).thenReturn(mockBankAccount);

		// ACT
		BankAccount response = bankAccountService.getBankAccountInformation(1);

		// ASSERT
		assertThat(response).isNotNull();
		assertThat(response.getIban()).isEqualTo("EU765504515816798265");
	}

	@Test
	void testGetBankAccountInformation_ShouldReturn_Null() {

		// ARRANGE
		when(bankAccountRepositoryMock.findByUserId(2)).thenReturn(null);

		// ACT
		BankAccount response = bankAccountService.getBankAccountInformation(2);

		// ASSERT
		assertThat(response).isNull();
	}

	@Test
	void testCreateBankAccount_ShouldReturnTrue() throws BankAccountServiceException {

		// ARRANGE
		when(bankAccountRepositoryMock.save(any(BankAccount.class))).thenReturn(mockBankAccount);

		// ACT
		boolean response = bankAccountService.saveBankAccountInformation(mockBankAccount);

		// ASSERT
		assertThat(response).isTrue();
	}

	@Test
	void testCreateBankAccount_ShouldThrowException() throws BankAccountServiceException {

		// ACT
		Executable executable = () -> bankAccountService.saveBankAccountInformation(null);

		// ASSERT
		assertThrows(BankAccountServiceException.class, executable);
		verify(bankAccountRepositoryMock, never()).save(null);
	}

	@Test
	void testDeleteBankAccount_ShouldReturn_True() throws BankAccountServiceException {

		// ARRANGE
		when(bankAccountRepositoryMock.findByUserId(anyInt())).thenReturn(mockBankAccount);
		Integer userId = mockUser.getId();

		// ACT
		boolean response = bankAccountService.deleteBankAccount(userId);

		// ASSERT
		assertThat(response).isTrue();
		verify(bankAccountRepositoryMock, times(1)).deleteBankAccountFromUserId(userId);
	}

	@Test
	void testDeleteBankAccount_ShouldThrow_Exception() throws BankAccountServiceException {

		// ARRANGE
		when(bankAccountRepositoryMock.findByUserId(anyInt())).thenReturn(null);
		Integer userId = mockUser.getId();

		// ACT
		Executable executable = () -> bankAccountService.deleteBankAccount(userId);

		// ASSERT
		assertThrows(BankAccountServiceException.class, executable);
		verify(bankAccountRepositoryMock, never()).deleteBankAccountFromUserId(userId);
	}

	@Test
	void updateBankAccount_ShouldReturn_True() throws BankAccountServiceException {

		// ARRANGE
		// In our case, we only update the name of the bank, IBAN/RIB stay the same
		when(bankAccountRepositoryMock.findByUserId(anyInt())).thenReturn(mockBankAccount);
		BankAccountAddDto bankAccountAddDto = new BankAccountAddDto();
		bankAccountAddDto.setBankName("French Bank");
		bankAccountAddDto.setIban(mockBankAccount.getIban());
		bankAccountAddDto.setRib(mockBankAccount.getRib());

		// ACT
		boolean response = bankAccountService.updateBankAccountInformation(mockUser.getId(), bankAccountAddDto);
		BankAccount bankAccountResponse = bankAccountService.getBankAccountInformation(1);

		// ASSERT
		assertThat(response).isTrue();
		assertThat(bankAccountResponse.getBankName()).isEqualTo("French Bank");
	}

	@Test
	void updateBankAccount_ShouldThrow_Exception() {

		// ARRANGE
		when(bankAccountRepositoryMock.findByUserId(anyInt())).thenReturn(null);
		BankAccountAddDto bankAccountAddDto = new BankAccountAddDto();
		bankAccountAddDto.setBankName("French Bank");
		bankAccountAddDto.setIban(mockBankAccount.getIban());
		bankAccountAddDto.setRib(mockBankAccount.getRib());

		// ACT
		Executable executable = () -> bankAccountService.updateBankAccountInformation(mockUser.getId(),
				bankAccountAddDto);

		// ASSERT
		assertThrows(BankAccountServiceException.class, executable);
	}

}
