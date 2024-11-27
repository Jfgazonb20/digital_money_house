package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Endpoint para listar todas las actividades
    @GetMapping("/{id}/activity")
    public ResponseEntity<List<Transaction>> getAllActivities(@PathVariable Long id) {
        List<Transaction> transactions = transactionService.getAllTransactionsByAccountId(id);
        return ResponseEntity.ok(transactions);
    }

    // Endpoint para obtener el detalle de una actividad específica
    @GetMapping("/{accountId}/activity/{transactionId}")
    public ResponseEntity<Transaction> getActivityDetails(
            @PathVariable Long accountId, @PathVariable Long transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }
}
