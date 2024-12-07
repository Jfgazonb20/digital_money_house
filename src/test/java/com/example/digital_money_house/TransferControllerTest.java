package com.example.digital_money_house;

import com.example.digital_money_house.Controller.TransferController;
import com.example.digital_money_house.Exception.CustomGoneException;
import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Service.TransferService;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    private final TransferService transferService = Mockito.mock(TransferService.class);
    private final JwtService jwtService = Mockito.mock(JwtService.class);
    private final TransferController transferController = new TransferController(transferService, jwtService);

    @Test
    void transferMoney_InsufficientFunds() {
        String token = "Bearer valid.jwt.token";
        when(jwtService.extractUsername("valid.jwt.token")).thenReturn("testuser");
        doNothing().when(transferService).validateUserAccessToAccount(1L, "testuser");

        when(transferService.transferMoney(1L, "alias.destino", 500.00, "Pago Servicio"))
                .thenThrow(new CustomGoneException("Fondos insuficientes"));

        ResponseEntity<?> response = transferController.transferMoney(1L, "alias.destino", 500.00, "Pago Servicio", token);

        assertThat(response.getStatusCodeValue()).isEqualTo(410);
        assertThat(response.getBody()).isEqualTo("Fondos insuficientes");
    }

    @Test
    void getRecentTransfers_NoPermission() {
        String token = "Bearer valid.jwt.token";
        when(jwtService.extractUsername("valid.jwt.token")).thenReturn("unauthorizedUser");
        doThrow(new IllegalArgumentException("No tiene permisos para acceder a esta cuenta"))
                .when(transferService).validateUserAccessToAccount(1L, "unauthorizedUser");

        ResponseEntity<?> response = transferController.getRecentTransfers(1L, token);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
        assertThat(response.getBody()).isEqualTo("No tiene permisos para acceder a esta cuenta");
    }
}
