package br.com.pagamento.controller.account;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pagamento.dto.account.AccountDTO;
import br.com.pagamento.dto.account.AvailableLimitDTO;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.service.account.AccountService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/accounts/limits")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountDTO> findAll() {
        return accountService.findAll()
                             .stream()
                             .map(account -> convertToDTO(account))
                             .collect(Collectors.toList());
    }

    @GetMapping("/{account_id}")
    public ResponseEntity<AccountDTO> findById(@PathVariable(name = "account_id", required = true) Long id) {
        return accountService.findById(id)
                             .map(account -> ResponseEntity.ok(convertToDTO(account)))
                             .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<AccountDTO> create(@RequestBody(required = true) AccountDTO accountDTO) {
        return accountService.create(convertToAccount(accountDTO))
                             .map(account -> ResponseEntity.ok(convertToDTO(account)))
                             .get();
    }

    @PatchMapping("/{account_id}")
    public ResponseEntity<AccountDTO> update(@PathVariable(name = "account_id", required = true) Long id, @RequestBody AccountDTO accountDTO) {
        Account account = convertToAccount(accountDTO);
        account.setId(id);
        return accountService.updateLimits(account)
                             .map(item -> ResponseEntity.ok(convertToDTO(item)))
                             .get();
    }
    
    public AccountDTO convertToDTO(Account account) {
    	return AccountDTO.builder().id(account.getId())
		                 .availableCreditLimit(new AvailableLimitDTO(account.getAvailableCreditLimit()))
		                 .availableWithdrawalLimit(new AvailableLimitDTO(account.getAvailableWithdrawalLimit()))
		                 .build();
    }
    
    public Account convertToAccount(AccountDTO accountDTO) {
    	return Account.builder().id(accountDTO.getId())
								.availableCreditLimit(Optional.ofNullable(accountDTO.getAvailableCreditLimit())
															  .map(availableCreditLimit -> availableCreditLimit.getAmount())
															  .orElse(null))
								.availableWithdrawalLimit(Optional.ofNullable(accountDTO.getAvailableWithdrawalLimit())
															  .map(availableWithdrawalLimit -> availableWithdrawalLimit.getAmount())
															  .orElse(null))
				                .build();
    }
}