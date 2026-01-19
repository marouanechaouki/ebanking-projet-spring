package fr.cnam.ebankingbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.cnam.ebankingbackend.entities.BankAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
public class CustomerDto {

    private Long id;
    private String name;
    private String email;

}
