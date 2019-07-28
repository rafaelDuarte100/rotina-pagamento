package br.com.pagamento.transaction.controller;

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

import br.com.pagamento.transaction.dto.TransactionDTO;
import br.com.pagamento.transaction.dto.TransactionPostDTO;
import br.com.pagamento.transaction.services.TransactionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionDTO> findAll() {
        return transactionService.findAll()
                                 .stream()
                                 .map(transaction -> transaction.toTransactionDTO())
                                 .collect(Collectors.toList());
    }

    @GetMapping("/{transaction_id}")
    public ResponseEntity<TransactionDTO> findById(@PathVariable(name = "transaction_id", required = true) Long id) {
        return transactionService.findById(id)
                                 .map(transaction -> ResponseEntity.ok(transaction.toTransactionDTO()))
                                 .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TransactionDTO> create(@RequestBody TransactionPostDTO transactionPostDTO) {
        return transactionService.create(transactionPostDTO.toTransaction())
                                 .map(transaction -> ResponseEntity.status(HttpStatus.CREATED).body(transaction.toTransactionDTO()))
                                 .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}