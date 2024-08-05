package com.xingcdev.museum.services;

import com.xingcdev.museum.domain.entities.Account;
import com.xingcdev.museum.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> findAll() {
        // Convert Iterable to List
        return StreamSupport.stream(accountRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Optional<Account> findOne(UUID id) {
        return accountRepository.findById(id);
    }

}
