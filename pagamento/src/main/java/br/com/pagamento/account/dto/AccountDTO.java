package br.com.pagamento.account.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.pagamento.account.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("account_id")
    private long id;

    @JsonProperty("available_credit_limit")
    private AvailableLimitDTO availableCreditLimit;

    @JsonProperty("available_withdrawal_limit")
    private AvailableLimitDTO availableWithdrawalLimit;

    public static AccountDTO toAccountDTO(Account account) {
        return builder().id(account.getId())
                        .availableCreditLimit(new AvailableLimitDTO(account.getAvailableCreditLimit()))
                        .availableWithdrawalLimit(new AvailableLimitDTO(account.getAvailableWithdrawalLimit()))
                        .build();
    }
}