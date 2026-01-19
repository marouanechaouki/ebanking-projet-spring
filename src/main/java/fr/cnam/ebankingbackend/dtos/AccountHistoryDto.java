package fr.cnam.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDto {
    private String accountId;
    private double balance;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private List<AccountOperationDto>  accountOperationDtos;

}
