package fr.cnam.ebankingbackend.repositories;

import fr.cnam.ebankingbackend.entities.AccountOperation;
import fr.cnam.ebankingbackend.entities.BankAccount;
import fr.cnam.ebankingbackend.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
    List<AccountOperation> findByBankAccount_Id(String accountId);
    //Pareil juste au lieu de donner un accountId on donne l'objet bankAccount
    //List<AccountOperation> findByBankAccount(BankAccount bankAccount);

    Page<AccountOperation> findByBankAccount_IdOrderByOperationDateDesc(String accountId, Pageable pageable);
    Page<AccountOperation> findByBankAccount(BankAccount bankAccount, Pageable pageable);
}
