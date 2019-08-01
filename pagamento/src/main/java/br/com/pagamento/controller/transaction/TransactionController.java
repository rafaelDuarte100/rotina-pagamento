package br.com.pagamento.controller.transaction;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pagamento.dto.transaction.TransactionDTO;
import br.com.pagamento.dto.transaction.TransactionPostDTO;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationType;
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
                                 .map(transaction -> convertToDTO(transaction))
                                 .collect(Collectors.toList());
    }

    @GetMapping("/transactions/{id}")
    public TransactionDTO findById(@PathVariable(name = "id", required = true) Long id) {
        return convertToDTO(transactionService.findById(id));
    }
    
    @PostMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> create(@RequestBody(required = true) TransactionPostDTO transactionPostDTO) {
        return ResponseEntity.ok(transactionService.create(convertToTransaction(transactionPostDTO))
                                                   .stream()
                                                   .map(transaction -> convertToDTO(transaction))
                                                   .collect(Collectors.toList()));
    }

    @PostMapping("/payments")
    public ResponseEntity<List<TransactionDTO>> createPayments(@RequestBody(required = true) List<TransactionPostDTO> payments) {
        List<Transaction> transactions = payments.stream()
                                                 .map(dto -> convertToPayment(dto))
                                                 .collect(Collectors.toList());

        List<TransactionDTO> dtos =  transactionService
                                        .createPayments(transactions)
                                        .stream()
                                        .map(transaction -> convertToDTO(transaction))
                                        .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    public TransactionDTO convertToDTO(Transaction transaction) {
        return TransactionDTO.builder()
                             .id(transaction.getId())
                             .accountId(transaction.getAccount().getId())
                             .operationTypeId(transaction.getOperationType().getId())
                             .amount(transaction.getAmount())
                             .balance(transaction.getBalance())
                             .eventDate(transaction.getEventDate())
                             .dueDate(transaction.getDueDate())
                             .build();
    }

    public Transaction convertToPayment(TransactionPostDTO transactionPostDTO) {
        return convertToTransaction(transactionPostDTO, OperationType.PAGAMENTO);
    }

    public Transaction convertToTransaction(TransactionPostDTO transactionPostDTO) {
        return convertToTransaction(transactionPostDTO, transactionPostDTO.getOperationTypeId());
    }

    public Transaction convertToTransaction(TransactionPostDTO transactionPostDTO, Long operationTypeId) {
        return Transaction.builder()
                          .id(0L)
                          .account(Account.builder()
                                          .id(transactionPostDTO.getAccountId())
                                          .build())
                          .operationType(OperationType.builder()
                                                      .id(operationTypeId)
                                                      .build())
                          .amount(transactionPostDTO.getAmount())
                          .balance(transactionPostDTO.getAmount())
                          .build();
    }
}