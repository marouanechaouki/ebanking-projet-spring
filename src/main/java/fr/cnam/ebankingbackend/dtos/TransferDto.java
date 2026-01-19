package fr.cnam.ebankingbackend.dtos;

import lombok.Data;

@Data
public class TransferDto {
    private String accountIdSource;
    private String accountIdDestination;
    private double amount;
}
