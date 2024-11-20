package com.example.digital_money_house.Service;

import com.example.digital_money_house.Exception.ResourceNotFoundException;
import com.example.digital_money_house.Model.Account;
import com.example.digital_money_house.Repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public Account updateAccount(Long id, Account updatedAccountData) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        if (updatedAccountData.getAccountNumber() != null) {
            account.setAccountNumber(updatedAccountData.getAccountNumber());
        }

        if (updatedAccountData.getBalance() != null) {
            account.setBalance(updatedAccountData.getBalance());
        }

        accountRepository.save(account);
        return account;
    }


    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
    }
}
