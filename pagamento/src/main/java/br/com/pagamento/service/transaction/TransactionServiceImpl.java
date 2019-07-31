package br.com.pagamento.service.transaction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.model.transaction.OperationCategory;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.transaction.TransactionRepository;
import br.com.pagamento.service.account.AccountService;
import br.com.pagamento.validator.transaction.TransactionValidator;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final TransactionOperationFactory transactionOperationFactory;
	private final AccountService accountService;
	private final TransactionValidator validator;

    @Override
	public List<Transaction> findAll() {
		return transactionRepository.findAll();
	}

	@Override
	public Optional<Transaction> findById(Long id) {
		return transactionRepository.findById(id);
	}

	@Override
	public Optional<Transaction> create(Transaction transaction) {
		/*return Optional.of( transactionRepository.save(transaction));*/
		
		validate(Arrays.asList(transaction));
		loadAccounts(Arrays.asList(transaction));
		List<Transaction> populatedTransactions = populeTransactions(transaction.getOperationType().getCategory(), transaction);
		updateAccounts(populatedTransactions);
		saveTransactions(populatedTransactions);
		return populatedTransactions.stream()
									.filter(item -> item.getId() == transaction.getId())
									.findFirst();
	}

	@Override
	public List<Transaction> createPayments(List<Transaction> transactions) {
		validate(transactions);
		loadAccounts(transactions);
		List<Transaction> populatedTransactions = populeTransactions(OperationCategory.PAGAMENTO, transactions.toArray(new Transaction[0]));
		updateAccounts(populatedTransactions);
		saveTransactions(populatedTransactions);
		return populatedTransactions;
	}

	private void validate(List<Transaction> transactions) {
		transactions.stream()
					.forEach(item -> validator.basicValidations(item));
	}

	private void loadAccounts(List<Transaction> transactions) {
		transactions.stream()
					.forEach(item -> item.setAccount(accountService.findById(item.getAccount().getId()).get()));
	}

	private List<Transaction> populeTransactions(OperationCategory operationCategory, Transaction...transactions) {
		return transactionOperationFactory.getTransactionOperation(operationCategory)
										  .populeTransactions(transactions);
	}

	private void saveTransactions(List<Transaction> populatedTransactions) {
		for (Transaction transaction : populatedTransactions) {
			transactionRepository.save(transaction);
		}
	}

	private void updateAccounts(List<Transaction> transactions) {
		transactions.stream()
					.map(item -> item.getAccount())
					.distinct()
					.forEach(account -> accountService.update(account));
	}
}