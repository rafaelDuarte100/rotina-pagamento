package br.com.pagamento.model.transaction;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.pagamento.dto.transaction.TransactionDTO;
import br.com.pagamento.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1853880129659520981L;

    @Id
    @EqualsAndHashCode.Include
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="transaction_id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "operation_type_id")
    private OperationType operationType;

    private Double amount;

    private Double balance;

    @Column(name = "event_date")
    @Builder.Default
    private LocalDate eventDate = LocalDate.now();

    @Column(name = "due_date")
    private LocalDate dueDate;

    public TransactionDTO toTransactionDTO() {
        return TransactionDTO.builder()
                             .id(getId())
                             .accountId(getAccount().getId())
                             .operationTypeId(getOperationType().getId())
                             .amount(getAmount())
                             .balance(getBalance())
                             .eventDate(getEventDate())
                             .dueDate(getDueDate())
                             .build();
    }
}