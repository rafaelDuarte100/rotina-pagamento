package br.com.pagamento.service.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationCategory;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.account.AccountRepository;
import br.com.pagamento.validator.transaction.TransactionValidator;
import lombok.AllArgsConstructor;

@Component
@Transactional
@AllArgsConstructor
public class TransactionOperationPurchase implements TransactionOperation {

    private final AccountRepository accountRepository;
    private final TransactionValidator validator;

    @Override
	public Transaction populeTransaction(Transaction transaction) {
        Account account = loadAccount(transaction);
        validator.validateTransactionCashPurchase(transaction, account);        
        downAvailableCreditLimitAccount(transaction, account);
        inverterAmountSignalAndSetBalance(transaction);
        transaction.setAccount(account);
        return transaction;
    }

    private void inverterAmountSignalAndSetBalance(Transaction transaction) {
        transaction.setAmount(transaction.getAmount() * (-1));
        transaction.setBalance(transaction.getAmount());
    }

    private void downAvailableCreditLimitAccount(Transaction transaction, Account account) {
        account.setAvailableCreditLimit(account.getAvailableCreditLimit() - transaction.getAmount());
    }
    
    private Account loadAccount(Transaction transaction) {
        return accountRepository.findById(transaction.getAccount().getId()).get();
    }

    @Override
    public OperationCategory getOperationCategory() {
        return OperationCategory.COMPRA;
    }
}