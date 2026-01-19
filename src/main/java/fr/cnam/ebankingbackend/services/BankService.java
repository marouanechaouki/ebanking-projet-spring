package fr.cnam.ebankingbackend.services;

import fr.cnam.ebankingbackend.entities.BankAccount;
import fr.cnam.ebankingbackend.entities.CurrentAccount;
import fr.cnam.ebankingbackend.entities.SavingAccount;
import fr.cnam.ebankingbackend.repositories.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class BankService {

    private BankAccountRepository bankAccountRepository;
    public void consulter (){
        BankAccount bankAccount1 = bankAccountRepository.findById("92abbe6d-afd6-4a49-b7ba-4f6f8e12ad3d").orElse(null);
        System.out.println("########################################");
        System.out.println(bankAccount1.getCustomer().getName());
        System.out.println(bankAccount1.getBalance());
        System.out.println(bankAccount1.getCreatedAt());
        System.out.println(bankAccount1.getBalance());
        System.out.println(bankAccount1.getStatus());
        if(bankAccount1 instanceof SavingAccount){
            System.out.println("InterestRate" + ((SavingAccount)bankAccount1).getInterestRate());
        }else if (bankAccount1 instanceof CurrentAccount){
            System.out.println("OverDraft" + ((CurrentAccount)bankAccount1).getOverDraft());
        }
        System.out.println("#########################################");
        bankAccount1.getAccountOperation().forEach(accountOperation1 -> {
            System.out.println(accountOperation1.getType()+"\t"+accountOperation1.getAmount()+"\t"+ accountOperation1.getOperationDate());
        });
    }
}
