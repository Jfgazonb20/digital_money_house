package com.example.digital_money_house;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.User;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Security.JwtService;
import com.example.digital_money_house.Service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    private final JwtService jwtService = Mockito.mock(JwtService.class);
    private final AccountService accountService = new AccountService(accountRepository, null, jwtService);

    @Test
    void validateUserAccessToAccount_Failure_NoPermission() {
        Account account = new Account();
        account.setId(1L);
        account.setUser(new User("user2"));

        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.of(account));
        when(jwtService.extractUsername("valid.token")).thenReturn("user1");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.validateUserAccessToAccount(1L, request);
        });

        assertThat(exception.getMessage()).isEqualTo("No tiene permisos para acceder a esta cuenta");
    }
}
