package com.example.digital_money_house.Controller;

import com.example.digital_money_house.Exception.CustomGoneException;
import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Service.TransferService;
import com.example.digital_money_house.Security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class TransferController {

    private final TransferService transferService;
    private final JwtService jwtService;

    public TransferController(TransferService transferService, JwtService jwtService) {
        this.transferService = transferService;
        this.jwtService = jwtService;
    }

    @PostMapping("/{id}/transferences")
    public ResponseEntity<?> transferMoney(
            @PathVariable Long id,
            @RequestParam String destination,
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestHeader("Authorization") String token) {
        try {
            String username = jwtService.extractUsername(token.replace("Bearer ", ""));
            transferService.validateUserAccessToAccount(id, username);

            Transaction transaction = transferService.transferMoney(id, destination, amount, description);
            return ResponseEntity.ok(transaction);
        } catch (CustomGoneException e) {
            return ResponseEntity.status(410).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/transferences/recent")
    public ResponseEntity<?> getRecentTransfers(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            String username = jwtService.extractUsername(token.replace("Bearer ", ""));
            transferService.validateUserAccessToAccount(id, username);

            List<Transaction> transactions = transferService.getRecentTransfers(id);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body("No tiene permisos para acceder a esta cuenta");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
