package com.xingcdev.museum.exceptions;

import lombok.Data;

@Data
public class AccountNotFoundException extends RuntimeException {
    private String code = "accountNotFound";
    public AccountNotFoundException(String id) {
        super("Could not find the account " + id + ". Please provide a valid id.");
    }
}
