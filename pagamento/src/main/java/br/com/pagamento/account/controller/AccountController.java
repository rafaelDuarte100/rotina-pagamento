package br.com.pagamento.account.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pagamento.account.dto.AccountDTO;
import static br.com.pagamento.account.dto.AccountDTO.toAccountDTO;
import br.com.pagamento.account.model.Account;
import br.com.pagamento.account.service.AccountService;
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
                             .map(account -> toAccountDTO(account))
                             .collect(Collectors.toList());
    }

    @GetMapping("/limits/{account_id}")
    public ResponseEntity<AccountDTO> findById(@PathVariable(name = "account_id", required = true) Long id) {
        return accountService.findById(id)
                             .map(account -> ResponseEntity.ok(toAccountDTO(account)))
                             .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/limits")
    public ResponseEntity<AccountDTO> create(@RequestBody AccountDTO newAccountDTO) {
        return accountService.create(Account.toAccount(newAccountDTO))
                             .map(account -> ResponseEntity.status(HttpStatus.CREATED).body(toAccountDTO(account)))
                             .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}