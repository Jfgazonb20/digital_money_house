package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transaction> getLastFiveTransactions(Long accountId) {
        validateAccountId(accountId);
        return transactionRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
    }

    public List<Transaction> getAllTransactionsByAccountId(Long accountId) {
        validateAccountId(accountId);
        return transactionRepository.findByAccountIdOrderByDateDesc(accountId);
    }

    public Transaction getTransactionById(Long transactionId) {
        if (transactionId == null || transactionId <= 0) {
            throw new IllegalArgumentException("El ID de la transacción debe ser un número positivo.");
        }
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transacción no encontrada con ID: " + transactionId));
    }

    // Método para validar el ID de la cuenta
    private void validateAccountId(Long accountId) {
        if (accountId == null || accountId <= 0) {
            throw new IllegalArgumentException("El ID de la cuenta debe ser un número positivo.");
        }

        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Cuenta no encontrada con ID: " + accountId);
        }
    }

}
