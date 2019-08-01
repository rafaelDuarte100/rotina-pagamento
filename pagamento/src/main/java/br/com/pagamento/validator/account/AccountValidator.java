package br.com.pagamento.validator.account;

import static java.util.Objects.nonNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.SourceMessage;
import br.com.pagamento.model.account.Account;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountValidator {

	private final SourceMessage messageSource;
	
	public Account validateAccountCreation(Account account) {
		return validateAvailableLimitLessThanZero(account, "conta.limite.informado.negativo");
	}
	
	public Account validateAccountUpdate(Account account) {
		return validateAvailableLimitLessThanZero(account, "conta.limite.negativo");
	}
	
	private Account validateAvailableLimitLessThanZero(Account account, String message) {
		Double availableCreditLimit = account.getAvailableCreditLimit();
		Double availableWithdrawal = account.getAvailableWithdrawalLimit();
		
		if ((nonNull(availableCreditLimit) && availableCreditLimit < 0)
				|| nonNull(availableWithdrawal) && availableWithdrawal < 0) {
			throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage(message));
		}
		return account;
	}
}
