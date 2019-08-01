package br.com.pagamento.service.transaction;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationCategory;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.transaction.TransactionRepository;
import br.com.pagamento.util.account.AccountUtil;
import br.com.pagamento.validator.transaction.TransactionValidator;

@Component
public class TransactionOperationWithdraw extends AbstractTransactionOperation {

    public TransactionOperationWithdraw(TransactionValidator validator, TransactionRepository transactionRepository,
                                        List<Transaction> resultado, AccountUtil accountUtil) {
        super(validator, transactionRepository, resultado, accountUtil);
    }

	@Override
    public void downAvailableLimitAccount(Transaction transaction, Account account) {
        account.setAvailableWithdrawalLimit(account.getAvailableWithdrawalLimit() + transaction.getBalance());
    }

    @Override
    public OperationCategory getOperationCategory() {
		return OperationCategory.SAQUE;
	}

	@Override
	protected void validate(Transaction transaction, Account account, Transaction positiveBalancePayment) {
		validator.validateTransactionWithdraw(transaction, account, positiveBalancePayment);
	}
}