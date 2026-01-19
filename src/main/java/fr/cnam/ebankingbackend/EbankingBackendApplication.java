package fr.cnam.ebankingbackend;

import fr.cnam.ebankingbackend.Enums.AccountStatus;
import fr.cnam.ebankingbackend.Enums.OperationType;
import fr.cnam.ebankingbackend.dtos.BankAccountDto;
import fr.cnam.ebankingbackend.dtos.CurrentBankAccountDto;
import fr.cnam.ebankingbackend.dtos.CustomerDto;
import fr.cnam.ebankingbackend.dtos.SavingBankAccountDto;
import fr.cnam.ebankingbackend.entities.*;
import fr.cnam.ebankingbackend.exceptions.BalanceNotSufficient;
import fr.cnam.ebankingbackend.exceptions.BankAccountNotFoundException;
import fr.cnam.ebankingbackend.exceptions.CustomerNotFoundException;
import fr.cnam.ebankingbackend.repositories.AccountOperationRepository;
import fr.cnam.ebankingbackend.repositories.BankAccountRepository;
import fr.cnam.ebankingbackend.repositories.CustomerRepository;
import fr.cnam.ebankingbackend.services.BankAccountService;
import fr.cnam.ebankingbackend.services.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication

public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner init(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Marouane", "Mehdi", "Lamia").forEach(name -> {
                CustomerDto customer = new CustomerDto();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.findAllCustomers().forEach(customer -> {
                try {
                    bankAccountService.CurrentBankAccount(Math.random() * 1000, 9000, customer.getId());
                    bankAccountService.SavingBankAccount(Math.random() * 1000, 5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDto> bankAccounts = bankAccountService.findAllBankAccounts();
            for (BankAccountDto bankAcc : bankAccounts) {
                for (int i = 0; i <10 ; i++) {
                    String accountId;
                    if(bankAcc instanceof SavingBankAccountDto) {
                        accountId = ((SavingBankAccountDto)bankAcc).getId();
                    }else {
                        accountId = ((CurrentBankAccountDto)bankAcc).getId();
                    }
                    try {
                        bankAccountService.credit(accountId, Math.random()*1000, "CREDIT");
                        bankAccountService.debit(accountId, Math.random()*100, "DEBIT");
                    } catch (BankAccountNotFoundException | BalanceNotSufficient e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };
    }


    //@Bean
    CommandLineRunner s (BankService bankService) {
        return args -> {
            /* bankService.consulter();
             */
            /*  Ce code a été deplacé vers la couche service : BankService*/
          /*  BankAccount bankAccount1 = bankAccountRepository.findById("92abbe6d-afd6-4a49-b7ba-4f6f8e12ad3d").orElse(null);
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
            });*/
        };
    }


    //@Bean
    CommandLineRunner start (CustomerRepository customerRepository, BankAccountRepository bankAccountRepository,
                             AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan","Lamiaa","Mehdi").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {
                        CurrentAccount currentAccount = new CurrentAccount();
                        currentAccount.setId(UUID.randomUUID().toString());
                        currentAccount.setBalance(Math.random() * 10000);
                        currentAccount.setCreatedAt(new Date());
                        currentAccount.setCustomer(customer);
                        currentAccount.setStatus(AccountStatus.ACTIVATED);
                        currentAccount.setOverDraft(8000);
                        bankAccountRepository.save(currentAccount);

                        SavingAccount savingAccount = new SavingAccount();
                        savingAccount.setId(UUID.randomUUID().toString());
                        savingAccount.setBalance(Math.random() * 10000);
                        savingAccount.setCreatedAt(new Date());
                        savingAccount.setCustomer(customer);
                        savingAccount.setStatus(AccountStatus.ACTIVATED);
                        savingAccount.setInterestRate(5.5);
                        bankAccountRepository.save(savingAccount);

                        bankAccountRepository.findAll().forEach(bankAccount -> {
                            AccountOperation accountOperation = new AccountOperation();
                            accountOperation.setOperationDate(new Date());
                            accountOperation.setAmount(Math.random() * 10000);
                            accountOperation.setType(Math.random()>0.5? OperationType.CREDIT:OperationType.DEBIT);
                            accountOperation.setBankAccount(bankAccount);
                            accountOperationRepository.save(accountOperation);

                        });
                    }
            );
        };
    }
}





