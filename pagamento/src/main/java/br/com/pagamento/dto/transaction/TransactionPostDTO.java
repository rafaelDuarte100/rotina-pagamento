package br.com.pagamento.dto.transaction;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationType;
import br.com.pagamento.model.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionPostDTO implements Serializable  {

    private static final long serialVersionUID = 5162704632400722086L;

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("operation_type_id")
    private Long operationTypeId;

    private Double amount;

    public Transaction toTransaction() {
        return Transaction.builder()
                          .id(0L)
                          .account(Account.builder().id(accountId).build())
                          .operationType(OperationType.builder().id(operationTypeId).build())
                          .amount(amount)
                          .balance(amount)
                          .build();
    }

    public Transaction toPayment() {
        return Transaction.builder()
                          .id(0L)
                          .account(Account.builder().id(accountId).build())
                          .operationType(OperationType.builder()
                                                      .id(OperationType.PAGAMENTO)
                                                      .build())
                          .amount(amount)
                          .balance(amount)
                          .build();
    }
}