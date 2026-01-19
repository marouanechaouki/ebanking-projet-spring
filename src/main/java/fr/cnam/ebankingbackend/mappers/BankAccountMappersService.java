package fr.cnam.ebankingbackend.mappers;

import fr.cnam.ebankingbackend.dtos.*;
import fr.cnam.ebankingbackend.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMappersService {
    public CustomerDto fromCustomer(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        /* Programation
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setId(customer.getId());*/
        return customerDto;
    };

    public Customer fromCustomerDto(CustomerDto customerDto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        /*customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setId(customerDto.getId());*/
        return customer;
    };

    public CurrentBankAccountDto fromCurrentBankAccount(CurrentAccount currentAccount) {
        CurrentBankAccountDto currentBankAccountDto = new CurrentBankAccountDto();
        BeanUtils.copyProperties(currentAccount, currentBankAccountDto);
        currentBankAccountDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDto.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDto;
    }

    public CurrentAccount fromCurrentBankAccountDto(CurrentBankAccountDto currentBankAccountDto) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDto, currentAccount);
        currentAccount.setCustomer(fromCustomerDto(currentBankAccountDto.getCustomerDto()));
        return currentAccount;
    }

    public SavingBankAccountDto fromSavingBankAccount(SavingAccount savingAccount) {
        SavingBankAccountDto savingBankAccountDto = new SavingBankAccountDto();
        BeanUtils.copyProperties(savingAccount, savingBankAccountDto);
        savingBankAccountDto.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDto.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDto;
    }

    public SavingAccount fromSavingBankAccountDto(SavingBankAccountDto savingBankAccountDto) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDto, savingAccount);
        savingAccount.setCustomer(fromCustomerDto(savingBankAccountDto.getCustomerDto()));
        return savingAccount;
    }

    public BankAccountDto fromBankAccount(BankAccount bankAccount) {
        if (bankAccount instanceof SavingAccount savingAccount) {
            return fromSavingBankAccount(savingAccount);
        }
        if (bankAccount instanceof CurrentAccount currentAccount) {
            return fromCurrentBankAccount(currentAccount);
        }
        throw new IllegalArgumentException(
                "Unsupported BankAccount type: " + bankAccount.getClass().getName()
        );
    }

    public AccountOperationDto fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDto operationDto = new AccountOperationDto();
        BeanUtils.copyProperties(accountOperation, operationDto);
        return operationDto;
    }
}
