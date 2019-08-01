package br.com.pagamento.controller.account;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/limits")
    public List<AccountDTO> findAll() {
        return accountService.findAll()
                             .stream()
                             .map(account -> convertToDTO(account))
                             .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AccountDTO findById(@PathVariable(name = "id", required = true) Long id) {
        return convertToDTO(accountService.findById(id));
    }
    
    @PostMapping
    public AccountDTO create(@RequestBody(required = true) AccountDTO accountDTO) {
        return convertToDTO(accountService.create(convertToAccount(accountDTO)));
    }

    @PatchMapping("/{id}")
    public AccountDTO update(@PathVariable(name = "id", required = true) Long id, @RequestBody AccountDTO accountDTO) {
        Account account = convertToAccount(accountDTO);
        account.setId(id);
        return convertToDTO(accountService.updateLimits(account));
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