package fr.cnam.ebankingbackend.web;

import fr.cnam.ebankingbackend.dtos.CustomerDto;
import fr.cnam.ebankingbackend.exceptions.CustomerNotFoundException;
import fr.cnam.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerResttController {

    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<CustomerDto> customers() {
        return bankAccountService.findAllCustomers();
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public CustomerDto customer(@PathVariable (name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @GetMapping("/customers/search")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<CustomerDto> searchCustomers(@RequestParam (name="keyword",defaultValue = "") String keyword) {
        return bankAccountService.searchCustomer("%"+keyword+"%");
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) throws CustomerNotFoundException {
        return bankAccountService.saveCustomer(customerDto);
    }

    @PutMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDto updateCustomer(@PathVariable Long id ,@RequestBody CustomerDto customerDto) throws CustomerNotFoundException {
        customerDto.setId(id);
        return bankAccountService.updateCustomer(customerDto);
    }

    /* Cas simple sans gestion exception :
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        bankAccountService.deleteCustomer(id);

    }*/
    /* Cas gestion error :*/
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        bankAccountService.deleteCustomer(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }



}
