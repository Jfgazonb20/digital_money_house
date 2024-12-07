package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.AccountService;
import com.example.digital_money_house.Service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class TransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    // Endpoint para listar todas las actividades
    @GetMapping("/{id}/activity")
    public ResponseEntity<List<Transaction>> getAllActivities(@PathVariable Long id, HttpServletRequest request) {
        accountService.validateUserAccessToAccount(id, request); // Validar acceso del usuario
        List<Transaction> transactions = transactionService.getAllTransactionsByAccountId(id);
        return ResponseEntity.ok(transactions);
    }

    // Endpoint para obtener el detalle de una actividad específica
    @GetMapping("/{accountId}/activity/{transactionId}")
    public ResponseEntity<Transaction> getActivityDetails(
            @PathVariable Long accountId,
            @PathVariable Long transactionId,
            HttpServletRequest request) {
        accountService.validateUserAccessToAccount(accountId, request); // Validar acceso del usuario
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }
}
