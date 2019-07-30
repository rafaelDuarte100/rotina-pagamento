package br.com.pagamento.dto.account;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = -3380702863059970039L;

	@JsonProperty("account_id")
    private long id;

    @JsonProperty("available_credit_limit")
    private AvailableLimitDTO availableCreditLimit;

    @JsonProperty("available_withdrawal_limit")
    private AvailableLimitDTO availableWithdrawalLimit;
    
}