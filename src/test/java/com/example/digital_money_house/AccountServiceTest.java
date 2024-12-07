package com.example.digital_money_house;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Model.Transaction;
import com.example.digital_money_house.Repository.AccountRepository;
import com.example.digital_money_house.Repository.TransactionRepository;
import com.example.digital_money_house.Service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
    private final AccountService accountService = new AccountService(accountRepository, transactionRepository);

    @Test
    void depositMoney_Success() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.00);

        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = accountService.depositMoney(1L, 500.00, "Depósito en cuenta");

        assertThat(transaction.getAmount()).isEqualTo(500.00);
        assertThat(account.getBalance()).isEqualTo(1500.00);
        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void depositMoney_Failure_AccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.depositMoney(1L, 500.00, "Depósito en cuenta");
        });
    }

    @Test
    void depositMoney_Failure_InvalidAmount() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.00);

        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> {
            accountService.depositMoney(1L, -100.00, "Depósito en cuenta");
        });
    }

    @Test
    void getAccountTransactions_Success() {
        Account account = new Account();
        account.setId(1L);

        when(accountRepository.existsById(1L)).thenReturn(true);
        accountService.getAccountTransactions(1L);

        verify(transactionRepository, times(1)).findByAccountIdOrderByDateDesc(1L);
    }

    @Test
    void getAccountTransactions_Failure_AccountNotFound() {
        when(accountRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountTransactions(2L);
        });
    }
}
