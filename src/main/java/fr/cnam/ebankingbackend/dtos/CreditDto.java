package fr.cnam.ebankingbackend.dtos;

import lombok.Data;

@Data
public class CreditDto {
    private double amount;
    private String description;
}
