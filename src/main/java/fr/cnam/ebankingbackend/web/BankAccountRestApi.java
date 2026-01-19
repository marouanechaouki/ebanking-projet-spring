package fr.cnam.ebankingbackend.web;

import fr.cnam.ebankingbackend.dtos.*;
import fr.cnam.ebankingbackend.exceptions.BalanceNotSufficient;
import fr.cnam.ebankingbackend.exceptions.BankAccountNotFoundException;
import fr.cnam.ebankingbackend.exceptions.CustomerNotFoundException;
import fr.cnam.ebankingbackend.services.BankAccountServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestApi {
    private BankAccountServiceImpl bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDto bankAccounts(@PathVariable (name = "accountId" ) String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }
    @GetMapping("/accounts")
    public List<BankAccountDto> bankAccounts() {
        return bankAccountService.findAllBankAccounts();
    }

    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDto> accountOperations(@PathVariable(name = "id") String accountId)  {
        return bankAccountService.historicalOperations(accountId);
    }

    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDto accountOperations(@PathVariable(name = "id") String accountId,
                                               @RequestParam (name="page",defaultValue = "0") int page,
                                               @RequestParam (name="size",defaultValue = "5") int size) {
        return bankAccountService.getAccountHistory(accountId,page,size);

    }

    @PostMapping("/accounts/{id}/debit")
    public void debited(@PathVariable("id") String accountId, @RequestBody DebitDto debitDto) throws BankAccountNotFoundException, BalanceNotSufficient {
        bankAccountService.debit(accountId,debitDto.getAmount(),debitDto.getDescription());
    }

    @PostMapping("/accounts/{id}/credit")
    public void credited(@PathVariable("id") String accountId, @RequestBody CreditDto creditDto) throws BankAccountNotFoundException {
        bankAccountService.credit(accountId,creditDto.getAmount(),creditDto.getDescription());
    }

    @PostMapping("/accounts/transfer")
    public void transfer (@RequestBody TransferDto transferDto) throws BankAccountNotFoundException, BalanceNotSufficient {
        bankAccountService.tranfer(transferDto.getAccountIdSource(),transferDto.getAmount(),transferDto.getAccountIdDestination());
    }

    @GetMapping("accounts/customer/{id}")
    public List<BankAccountDto> getAccountsByCustomer(@PathVariable("id") Long id) throws CustomerNotFoundException {
        return bankAccountService.findAllBankAccountsByCustomerId(id);
    }

}
