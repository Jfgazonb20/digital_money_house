package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/{id}/transferences")
    public ResponseEntity<Transaction> transferMoney(
            @PathVariable Long id,
            @RequestParam String destination,
            @RequestParam Double amount,
            @RequestParam String description) {
        Transaction transaction = transferService.transferMoney(id, destination, amount, description);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{id}/transferences")
    public ResponseEntity<List<Transaction>> getRecentTransfers(@PathVariable Long id) {
        List<Transaction> transactions = transferService.getRecentTransfers(id);
        return ResponseEntity.ok(transactions);
    }
}

