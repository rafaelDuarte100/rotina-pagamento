package br.com.pagamento.util.account;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

import br.com.pagamento.model.account.Account;

@Component
public class AccountUtil {

	public void updateAccountLimits(Account account, Double creditLimit, Double WithdrawalLimit) {
		account.setAvailableCreditLimit(addAccountCreditLimit(account, creditLimit));
		account.setAvailableWithdrawalLimit(addAccountWithdrawalLimit(account, WithdrawalLimit));
	}
	
	public Double addAccountCreditLimit(Account account, Double creditLimit) {
		return sumAvailableLimit(account.getAvailableCreditLimit(), creditLimit);
	}
	
	public Double addAccountWithdrawalLimit(Account account, Double WithdrawalLimit) {
		return sumAvailableLimit(account.getAvailableWithdrawalLimit(), WithdrawalLimit);
	}
	
	public Double sumAvailableLimit(Double availableLimitCurrent, Double availableLimitAdd) {
		if (availableLimitCurrent == null) {
			return availableLimitAdd;
		}
		if (availableLimitAdd != null) {
			return truncateValue(availableLimitCurrent + availableLimitAdd);
		}
		return availableLimitCurrent;
	}
	
	public Double truncateValue(Double value) {
		return Double.valueOf(new DecimalFormat("#.##").format(value).replace(",", "."));
	}
}
