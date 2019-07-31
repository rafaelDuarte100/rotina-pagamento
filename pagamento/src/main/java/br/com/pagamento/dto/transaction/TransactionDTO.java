package br.com.pagamento.dto.transaction;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionDTO implements Serializable  {

    private static final long serialVersionUID = -5010944246657469113L;

    @JsonProperty("transaction_id")
    private Long id;
    
    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("operation_type_id")
    private Long operationTypeId;

    private Double amount;

    private Double balance;

    @JsonProperty("event_date")
    @Builder.Default
    private LocalDate eventDate = LocalDate.now();

    @JsonProperty("due_date")
    private LocalDate dueDate;

}