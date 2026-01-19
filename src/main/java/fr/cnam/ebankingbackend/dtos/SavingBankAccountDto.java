package fr.cnam.ebankingbackend.dtos;

import fr.cnam.ebankingbackend.Enums.AccountStatus;
import lombok.Data;

import java.util.Date;


@Data
public class SavingBankAccountDto extends BankAccountDto {

    private String id;
    private double balance;
    private Date createdAt;
    private String description;
    private AccountStatus status;
    private CustomerDto customerDto;
    private double interestRate;
}
