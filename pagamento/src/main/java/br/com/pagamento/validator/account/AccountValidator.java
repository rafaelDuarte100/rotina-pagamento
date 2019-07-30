package br.com.pagamento.validator.account;

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
	
	public void validateAccountCreation(Account account) {
		validateAvailableLimitLessThanZero(account, "conta.limite.informado.negativo");
	}
	
	public void validateAccountUpdate(Account account) {
		validateAvailableLimitLessThanZero(account, "conta.limite.negativo");
	}
	
	private void validateAvailableLimitLessThanZero(Account account, String message) {
		Double availableCreditLimit = account.getAvailableCreditLimit();
		Double availableWithdrawal = account.getAvailableWithdrawalLimit();
		
		if ((availableCreditLimit != null && availableCreditLimit < 0)
				|| availableWithdrawal != null && availableWithdrawal < 0) {
			throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage(message));
		}
	}
}
