package fr.cnam.ebankingbackend.services;

import fr.cnam.ebankingbackend.dtos.*;
import fr.cnam.ebankingbackend.exceptions.BalanceNotSufficient;
import fr.cnam.ebankingbackend.exceptions.BankAccountNotFoundException;
import fr.cnam.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDto saveCustomer(CustomerDto customerDto);

    CustomerDto getCustomer(Long id) throws CustomerNotFoundException;

    List<CustomerDto> searchCustomer(String kw);

    SavingBankAccountDto SavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    CurrentBankAccountDto CurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    List<CustomerDto> findAllCustomers();

    List<BankAccountDto> findAllBankAccounts();
    List<BankAccountDto> findAllBankAccountsByCustomerId(Long customerId) throws CustomerNotFoundException;

    BankAccountDto getBankAccount(String id) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BalanceNotSufficient, BankAccountNotFoundException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void tranfer(String accountIdSource, double amount, String accountIdDestination) throws BalanceNotSufficient, BankAccountNotFoundException;

    CustomerDto updateCustomer(CustomerDto customerDto);

    void deleteCustomer(Long id) throws CustomerNotFoundException;

    List<AccountOperationDto> historicalOperations(String accountId);

    AccountHistoryDto getAccountHistory(String accountId, int page, int size);

}
