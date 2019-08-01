package br.com.pagamento.util.account;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

import br.com.pagamento.model.account.Account;

@Component
public class AccountUtil {

	public Account updateAccountLimits(Account accountDB, Account newAccount) {
		accountDB.setAvailableCreditLimit(addAccountCreditLimit(accountDB, newAccount.getAvailableCreditLimit()));
		accountDB.setAvailableWithdrawalLimit(addAccountWithdrawalLimit(accountDB, newAccount.getAvailableWithdrawalLimit()));
		return accountDB;
	}
	
	public Double addAccountCreditLimit(Account account, Double creditLimit) {
		return sumAvailableLimit(account.getAvailableCreditLimit(), creditLimit);
	}
	
	public Double addAccountWithdrawalLimit(Account account, Double WithdrawalLimit) {
		return sumAvailableLimit(account.getAvailableWithdrawalLimit(), WithdrawalLimit);
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
