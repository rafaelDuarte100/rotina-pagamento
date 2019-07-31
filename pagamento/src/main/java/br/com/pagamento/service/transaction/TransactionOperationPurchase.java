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
public class TransactionOperationPurchase extends AbstractTransactionOperation {
    
    public TransactionOperationPurchase(TransactionValidator validator, TransactionRepository transactionRepository,
                                        List<Transaction> resultado, AccountUtil accountUtil) {
        super(validator, transactionRepository, resultado, accountUtil);
    }

	@Override
    public void downAvailableLimitAccount(Transaction transaction, Account account) {
        account.setAvailableCreditLimit(account.getAvailableCreditLimit() + transaction.getBalance());
    }

    @Override
    public OperationCategory getOperationCategory() {
        return OperationCategory.COMPRA;
    }
}