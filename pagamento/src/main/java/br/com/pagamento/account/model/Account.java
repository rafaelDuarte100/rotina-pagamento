package br.com.pagamento.account.model;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.pagamento.account.dto.AccountDTO;
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
@Table(name="account")
public class Account implements Serializable {
	
	private static final long serialVersionUID = -2891410009810697423L;

	@Id
    @EqualsAndHashCode.Include
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="account_id")
	private Long id;
	
	@Column(name="available_credit_limit")
	private Double availableCreditLimit;
	
	@Column(name="available_withdrawal_limit")
	private Double availableWithdrawalLimit;
	
	public static Account toAccount(AccountDTO accountDTO) {
        return builder().id(accountDTO.getId())
						.availableCreditLimit(Optional.ofNullable(accountDTO.getAvailableCreditLimit())
													  .map(availableCreditLimit -> availableCreditLimit.getAmount())
													  .orElse(null))
						.availableWithdrawalLimit(Optional.ofNullable(accountDTO.getAvailableWithdrawalLimit())
													  .map(availableWithdrawalLimit -> availableWithdrawalLimit.getAmount())
													  .orElse(null))
                        .build();
    }
}