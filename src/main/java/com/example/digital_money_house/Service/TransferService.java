package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // Obtener destinatarios recientes
    public List<Transaction> getRecentTransfers(Long accountId) {
        return transactionRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
    }

    // Realizar transferencia
    @Transactional
    public Transaction transferMoney(Long sourceAccountId, String destinationCvuOrAlias, Double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta origen no encontrada con ID: " + sourceAccountId));

        Account destinationAccount = accountRepository.findByCvuOrAlias(destinationCvuOrAlias, destinationCvuOrAlias)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta destino no encontrada con CVU/Alias: " + destinationCvuOrAlias));

        if (sourceAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Fondos insuficientes");
        }

        // Restar saldo a la cuenta origen
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        accountRepository.save(sourceAccount);

        // Sumar saldo a la cuenta destino
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        accountRepository.save(destinationAccount);

        // Registrar transacción
        Transaction transaction = new Transaction();
        transaction.setAccount(sourceAccount);
        transaction.setAmount(-amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(description + " -> " + destinationAccount.getAlias());
        transactionRepository.save(transaction);

        return transaction;
    }
}
