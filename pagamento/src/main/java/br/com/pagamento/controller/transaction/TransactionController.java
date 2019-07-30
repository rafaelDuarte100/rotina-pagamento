package br.com.pagamento.controller.transaction;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pagamento.dto.transaction.TransactionDTO;
import br.com.pagamento.dto.transaction.TransactionPostDTO;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.service.transaction.TransactionService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/transactions")
    public List<TransactionDTO> findAll() {
        return transactionService.findAll()
                                 .stream()
                                 .map(transaction -> transaction.toTransactionDTO())
                                 .collect(Collectors.toList());
    }

    @GetMapping("/transactions/{transaction_id}")
    public ResponseEntity<TransactionDTO> findById(@PathVariable(name = "transaction_id", required = true) Long id) {
        return transactionService.findById(id)
                                 .map(transaction -> ResponseEntity.ok(transaction.toTransactionDTO()))
                                 .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> create(@RequestBody(required = true) TransactionPostDTO transactionPostDTO) {
        return transactionService.create(transactionPostDTO.toTransaction())
                                 .map(transaction -> ResponseEntity.status(HttpStatus.CREATED).body(transaction.toTransactionDTO()))
                                 .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping("/payments")
    public ResponseEntity<List<TransactionDTO>> createPayments(@RequestBody(required = true) List<TransactionPostDTO> payments) {
        List<Transaction> transactions = payments.stream()
                                                 .map(dto -> dto.toPayment())
                                                 .collect(Collectors.toList());

        List<TransactionDTO> dtos =  transactionService
                                        .create(transactions)
                                        .get().stream()
                                        .map(transaction -> transaction.toTransactionDTO())
                                        .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }
}