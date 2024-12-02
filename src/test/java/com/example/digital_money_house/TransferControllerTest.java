package com.example.digital_money_house;

import com.example.digital_money_house.Controller.TransferController;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.TransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    private final TransferService transferService = Mockito.mock(TransferService.class);
    private final TransferController transferController = new TransferController(transferService);

    @Test
    void getRecentTransfers_Success() {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setAmount(-200.00);
        transaction.setDescription("Pago Servicio -> alias.destino");
        transactions.add(transaction);

        when(transferService.getRecentTransfers(1L)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transferController.getRecentTransfers(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getDescription()).isEqualTo("Pago Servicio -> alias.destino");

        verify(transferService, times(1)).getRecentTransfers(1L);
    }

    @Test
    void transferMoney_Success() {
        Transaction transaction = new Transaction();
        transaction.setAmount(-200.00);
        transaction.setDescription("Pago Servicio -> alias.destino");

        when(transferService.transferMoney(1L, "alias.destino", 200.00, "Pago Servicio")).thenReturn(transaction);

        ResponseEntity<Transaction> response = transferController.transferMoney(1L, "alias.destino", 200.00, "Pago Servicio");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getDescription()).isEqualTo("Pago Servicio -> alias.destino");

        verify(transferService, times(1)).transferMoney(1L, "alias.destino", 200.00, "Pago Servicio");
    }
}
