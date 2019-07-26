package br.com.pagamento.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pagamento.account.model.Account;
import br.com.pagamento.account.service.AccountService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/limits")
    public List<Account> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/limits/{account_id}")
    public ResponseEntity<Account> findById(@PathVariable(name = "account_id", required = true) Long id) {
        return accountService.findById(id)
                             .map(account -> ResponseEntity.ok(account))
                             .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/limits")
    public ResponseEntity<Account> create(@RequestBody Account newAccount) {
        return accountService.create(newAccount)
                             .map(account -> ResponseEntity.status(HttpStatus.CREATED).body(account))
                             .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}