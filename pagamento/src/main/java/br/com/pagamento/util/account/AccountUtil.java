package br.com.pagamento.util.account;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

import br.com.pagamento.model.account.Account;

@Component
public class AccountUtil {

	public Account updateAccountLimits(Account accountDB, Account newAccount) {
		accountDB.setAvailableCreditLimit(updateAccountCreditLimit(accountDB, newAccount.getAvailableCreditLimit()));
		accountDB.setAvailableWithdrawalLimit(updateAccountWithdrawalLimit(accountDB, newAccount.getAvailableWithdrawalLimit()));
		return accountDB;
	}
	
	public Double updateAccountCreditLimit(Account account, Double creditLimit) {
		if (nonNull(creditLimit) && creditLimit < 0) {
			return sumAvailableLimit(account.getAvailableCreditLimit(), creditLimit);
		}
		else {
			return creditLimit;
		}
	}
	
	public Double updateAccountWithdrawalLimit(Account account, Double withdrawalLimit) {
		if (nonNull(withdrawalLimit) && withdrawalLimit < 0) {
			return sumAvailableLimit(account.getAvailableWithdrawalLimit(), withdrawalLimit);
		}
		else {
			return withdrawalLimit;
		}
	}
	
	public Double sumAvailableLimit(Double availableLimitCurrent, Double availableLimitAdd) {
		if (isNull(availableLimitCurrent)) {
			return availableLimitAdd;
		}
		if (nonNull(availableLimitAdd)) {
			return truncateValue(availableLimitCurrent + availableLimitAdd);
		}
		return availableLimitCurrent;
	}
	
	public Double truncateValue(Double value) {
		return Double.valueOf(new DecimalFormat("#.##").format(value).replace(",", "."));
	}
}
