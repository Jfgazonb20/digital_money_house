package com.example.digital_money_house;

import com.example.digital_money_house.Controller.DashboardController;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private final AccountService accountService = Mockito.mock(AccountService.class);
    private final DashboardController dashboardController = new DashboardController(accountService);

    @Test
    void getAccountBalance_NoPermission() {
        doThrow(new IllegalArgumentException("No tienes permisos para acceder a esta cuenta"))
                .when(accountService).validateUserAccessToAccount(eq(2L), any(HttpServletRequest.class));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");

        ResponseEntity<Map<String, Object>> response = dashboardController.getAccountBalance(2L, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
        verify(accountService, times(1)).validateUserAccessToAccount(eq(2L), any(HttpServletRequest.class));
    }

}
