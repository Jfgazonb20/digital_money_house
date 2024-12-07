package com.example.digital_money_house;

import com.example.digital_money_house.Controller.AccountController;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private final AccountService accountService = Mockito.mock(AccountService.class);
    private final AccountController accountController = new AccountController(accountService);

    @BeforeEach
    void setUp() {
        // Simular el contexto HTTP para evitar errores relacionados con ServletRequestAttributes
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void depositMoney_Success() {
        Transaction transaction = new Transaction();
        transaction.setAmount(500.00);
        transaction.setDescription("Depósito en cuenta");

        when(accountService.depositMoney(1L, 500.00, "Depósito en cuenta")).thenReturn(transaction);

        ResponseEntity<Transaction> response = accountController.depositMoney(1L, 500.00, "Depósito en cuenta");

        assertThat(response.getStatusCodeValue()).isEqualTo(201); // Cambiado de 200 a 201
        assertThat(response.getBody().getAmount()).isEqualTo(500.00);
        verify(accountService, times(1)).depositMoney(1L, 500.00, "Depósito en cuenta");
    }


    @Test
    void depositMoney_Failure_AccountNotFound() {
        when(accountService.depositMoney(2L, 500.00, "Depósito en cuenta"))
                .thenThrow(new RuntimeException("Cuenta no encontrada con ID: 2"));

        try {
            accountController.depositMoney(2L, 500.00, "Depósito en cuenta");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Cuenta no encontrada con ID: 2");
        }
    }
}
