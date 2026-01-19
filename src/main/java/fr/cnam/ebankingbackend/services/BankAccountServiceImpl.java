package fr.cnam.ebankingbackend.services;

import fr.cnam.ebankingbackend.Enums.AccountStatus;
import fr.cnam.ebankingbackend.Enums.OperationType;
import fr.cnam.ebankingbackend.dtos.*;
import fr.cnam.ebankingbackend.entities.*;
import fr.cnam.ebankingbackend.exceptions.BalanceNotSufficient;
import fr.cnam.ebankingbackend.exceptions.BankAccountNotFoundException;
import fr.cnam.ebankingbackend.exceptions.CustomerNotFoundException;
import fr.cnam.ebankingbackend.mappers.BankAccountMappersService;
import fr.cnam.ebankingbackend.repositories.AccountOperationRepository;
import fr.cnam.ebankingbackend.repositories.BankAccountRepository;
import fr.cnam.ebankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMappersService bankAccountMappersService;
    //@Slf4j permet de créer un objet log sans passer par Logger ...
    //Logger log= LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        //log.info("Saving customer "+customer);
        Customer customer = bankAccountMappersService.fromCustomerDto(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMappersService.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDto getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return bankAccountMappersService.fromCustomer(customer);

    }

    @Override
    public List<CustomerDto> searchCustomer(String keyword){
        List<Customer> customerList = customerRepository.searchCustomerByKW(keyword);
        List<CustomerDto> customerDtos= customerList.stream().map(cust->bankAccountMappersService.fromCustomer(cust)).collect(Collectors.toList());
        return customerDtos;
    }

    @Override
    public SavingBankAccountDto SavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setCustomer(customer);
        savingAccount.setBalance(initialBalance);
        savingAccount.setStatus(AccountStatus.ACTIVATED);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setId(UUID.randomUUID().toString());
        SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);
        return bankAccountMappersService.fromSavingBankAccount(savedSavingAccount);
    }

    @Override
    public CurrentBankAccountDto CurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCustomer(customer);
        currentAccount.setBalance(initialBalance);
        currentAccount.setStatus(AccountStatus.ACTIVATED);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedcurrentAccount = bankAccountRepository.save(currentAccount);
        return bankAccountMappersService.fromCurrentBankAccount(savedcurrentAccount);
    }

    @Override
    public List<CustomerDto> findAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();
        /* Programation imperative
        List<CustomerDto> customerDtoList = new ArrayList<>();
        for (Customer customer : customerList) {
            CustomerDto customerDto = bankAccountMappersService.fromCustomer(customer);
            customerDtoList.add( customerDto);

        }*/
        /* Programmation fonctionnel  */
        List<CustomerDto> customerDtoList = customerList.stream().map(customer -> bankAccountMappersService.fromCustomer(customer)).collect(Collectors.toList());
        return customerDtoList;
    }

    @Override
    public BankAccountDto getBankAccount(String id) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return bankAccountMappersService.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return bankAccountMappersService.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficient, BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficient("Balance not sufficient !!!");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setType(OperationType.DEBIT);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setType(OperationType.CREDIT);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void tranfer(String accountIdSource, double amount, String accountIdDestination) throws BalanceNotSufficient, BankAccountNotFoundException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "transfer from " + accountIdSource);

    }

    @Override
    public List<BankAccountDto> findAllBankAccounts() {
        List<BankAccount> bankAccount = bankAccountRepository.findAll();
        List<BankAccountDto> bankAccountDtoList = bankAccount.stream().map(account -> {
            if (account instanceof CurrentAccount) {
                CurrentAccount currentAccount = (CurrentAccount) account;
                return bankAccountMappersService.fromCurrentBankAccount(currentAccount);

            } else {
                SavingAccount savingAccount = (SavingAccount) account;
                return bankAccountMappersService.fromSavingBankAccount(savingAccount);

            }
        }).collect(Collectors.toList());
        return bankAccountDtoList;
    }

    @Override
    public List<BankAccountDto> findAllBankAccountsByCustomerId(Long customerId) throws CustomerNotFoundException {
        CustomerDto customerDto = getCustomer(customerId);
        List<BankAccount> bankAccountByCustomerId = bankAccountRepository.findBankAccountByCustomerId(customerDto.getId());
        return bankAccountByCustomerId.stream().map(bankAccount -> bankAccountMappersService.fromBankAccount(bankAccount)).toList();
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        //log.info("Saving customer "+customer);
        Customer customer = bankAccountMappersService.fromCustomerDto(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMappersService.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + customerId + " not found"));
        customerRepository.delete(customer);
    }

    @Override
    public List<AccountOperationDto> historicalOperations(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccount_Id(accountId);
        return accountOperations.stream().map(op ->
                bankAccountMappersService.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDto getAccountHistory(String accountId, int page, int size) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccount_IdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        List<AccountOperationDto> accountOperationDtos = accountOperations.stream().map(op -> bankAccountMappersService.fromAccountOperation(op)).collect(Collectors.toList());
        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();
        accountHistoryDto.setAccountId(bankAccount.getId());
        accountHistoryDto.setAccountOperationDtos(accountOperationDtos);
        accountHistoryDto.setBalance(bankAccount.getBalance());
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setPageSize(size);
        accountHistoryDto.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDto;
    }


}