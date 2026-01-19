package fr.cnam.ebankingbackend.dtos;

import fr.cnam.ebankingbackend.Enums.OperationType;
import fr.cnam.ebankingbackend.entities.BankAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
public class AccountOperationDto {

    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;


}
