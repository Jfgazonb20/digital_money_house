package com.example.digital_money_house.Service;

import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Obtener las últimas 5 transacciones
    public List<Transaction> getLastFiveTransactions(Long accountId) {
        return transactionRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
    }

    // Obtener todas las actividades de una cuenta
    public List<Transaction> getAllTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountIdOrderByDateDesc(accountId);
    }

    // Obtener el detalle de una transacción específica
    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));
    }
}
