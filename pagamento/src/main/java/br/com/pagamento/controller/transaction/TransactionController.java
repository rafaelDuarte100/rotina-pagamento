package br.com.pagamento.controller.transaction;

import java.util.List;
import java.util.stream.Collectors;

import br.com.pagamento.dto.account.AccountDTO;
import br.com.pagamento.messages.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.annotations.Parameter;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.pagamento.dto.transaction.TransactionDTO;
import br.com.pagamento.dto.transaction.TransactionPostDTO;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationType;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.service.transaction.TransactionService;
import lombok.AllArgsConstructor;

import static java.util.stream.Collectors.toList;

@RestController
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @ApiOperation(value = "Retorna todas as transações cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista todas as transações cadastradas.", response = AccountDTO[].class),
            @ApiResponse(code = 500, message = "Ocorreu um erro não esperado.", response = ErrorMessage.class),
    })
    @GetMapping(value = "/transactions", produces = "application/json")
    public List<TransactionDTO> findAll() {
        return transactionService.findAll()
                                 .stream()
                                 .map(transaction -> convertToDTO(transaction))
                                 .collect(toList());
    }

    @ApiOperation(value = "Busca uma transação por ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a transação encontrada pelo ID informado.", response = AccountDTO.class),
            @ApiResponse(code = 404, message = "A operação não pôde ser concluída porque a transação de Id informado, não existe.", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro não esperado.", response = ErrorMessage.class),
    })
    @GetMapping(value = "/transactions/{id}", produces = "application/json")
    public TransactionDTO findById(@PathVariable(name = "id") Long id) {
        return convertToDTO(transactionService.findById(id));
    }

    @ApiOperation(value = "Cria uma nova transação.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a transação cadastrada.", response = AccountDTO.class),
            @ApiResponse(code = 406, message = "A operação não pôde ser realizada devido alguma falha na validação dos dados informados, " +
                    "por exemplo conta inexistente, algum dado não informado, etc...", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro não esperado.", response = ErrorMessage.class),
    })
    @PostMapping(value = "/transactions", produces = "application/json")
    public ResponseEntity<List<TransactionDTO>> create(@RequestBody(required = true) TransactionPostDTO transactionPostDTO) {
        return ResponseEntity.ok(transactionService.create(convertToTransaction(transactionPostDTO))
                                                   .stream()
                                                   .map(transaction -> convertToDTO(transaction))
                                                   .collect(toList()));
    }

    @ApiOperation(value = "Cria uma lista de transações do tipo Pagamento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a lista de transações cadastradas.", response = AccountDTO.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro não esperado.", response = ErrorMessage.class),
    })
    @PostMapping(value = "/transactions/payments", produces = "application/json")
    public ResponseEntity<List<TransactionDTO>> createPayments(@RequestBody(required = true) List<TransactionPostDTO> payments) {
        List<Transaction> transactions = payments.stream()
                                                 .map(dto -> convertToPayment(dto))
                                                 .collect(toList());

        List<TransactionDTO> dtos =  transactionService
                                        .createPayments(transactions)
                                        .stream()
                                        .map(transaction -> convertToDTO(transaction))
                                        .collect(toList());

        return ResponseEntity.ok(dtos);
    }

    @ApiOperation(value = "Busca as transações de uma conta específica.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna as transações encontradas para a CONTA informada.", response = AccountDTO.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro não esperado.", response = ErrorMessage.class),
    })
    @GetMapping(value = "/transactions/account/{account_id}", produces = "application/json")
    public List<TransactionDTO> findAllByAccount(@PathVariable(name = "account_id") Long accountId) {
        return transactionService.findAllByAccount(accountId).stream()
                .map(this::convertToDTO)
                .collect(toList());
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