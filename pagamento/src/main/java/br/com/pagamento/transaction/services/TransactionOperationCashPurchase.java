package br.com.pagamento.transaction.services;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.account.model.Account;
import br.com.pagamento.account.repository.AccountRepository;
import br.com.pagamento.transaction.model.OperationType;
import br.com.pagamento.transaction.model.Transaction;
import br.com.pagamento.transaction.repository.TransactionRepository;
import br.com.pagamento.transaction.validator.TransactionValidator;
import lombok.AllArgsConstructor;

@Component
@Transactional
@AllArgsConstructor
public class TransactionOperationCashPurchase implements TransactionOperation {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionValidator validator;

    @Override
	public Optional<Transaction> insertTransaction(Transaction transaction) {
        Account account = loadAccount(transaction);
        validator.validateTransactionCashPurchase(transaction, account);        
        updateAccount(transaction, account);
        return saveTransaction(transaction);
    }

    private Optional<Transaction> saveTransaction(Transaction transaction) {
        transaction.setAmount(transaction.getAmount() * (-1));
        transaction.setBalance(transaction.getAmount());

		return Optional.ofNullable(transactionRepository.save(transaction));
    }

    private void updateAccount(Transaction transaction, Account account) {
        account.setAvailableCreditLimit(account.getAvailableCreditLimit() - transaction.getAmount());
        accountRepository.save(account);
    }
    
    private Account loadAccount(Transaction transaction) {
        return accountRepository.findById(transaction.getAccount().getId()).get();
    }

    @Override
    public Long getOperationType() {
        return OperationType.COMPRA_A_VISTA;
    }
}