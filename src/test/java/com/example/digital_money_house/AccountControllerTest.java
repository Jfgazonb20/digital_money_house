package com.example.digital_money_house;

import com.example.digital_money_house.Controller.AccountController;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private final AccountService accountService = Mockito.mock(AccountService.class);
    private final AccountController accountController = new AccountController(accountService);

    @Test
    void depositMoney_Success() {
        Transaction transaction = new Transaction();
        transaction.setAmount(500.00);
        transaction.setDescription("Depósito en cuenta");

        when(accountService.depositMoney(1L, 500.00, "Depósito en cuenta")).thenReturn(transaction);

        ResponseEntity<Transaction> response = accountController.depositMoney(1L, 500.00, "Depósito en cuenta");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getAmount()).isEqualTo(500.00);
        verify(accountService, times(1)).depositMoney(1L, 500.00, "Depósito en cuenta");
    }
}
