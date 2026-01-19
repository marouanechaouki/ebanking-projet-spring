package fr.cnam.ebankingbackend.repositories;

import fr.cnam.ebankingbackend.entities.BankAccount;
import fr.cnam.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    List<BankAccount> findBankAccountByCustomerId(Long customerId);
}
