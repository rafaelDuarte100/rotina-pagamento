package br.com.pagamento.service.transaction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.exception.TransactionNotFoundException;
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
	public Transaction findById(Long id) {
		return transactionRepository.findById(id)
									.orElseThrow(() -> new TransactionNotFoundException(id));
	}

	@Override
	public List<Transaction> create(Transaction transaction) {
		validate(Arrays.asList(transaction));
		loadAccounts(Arrays.asList(transaction));
		List<Transaction> populatedTransactions = populeTransactions(transaction.getOperationType().getCategory(), Arrays.asList(transaction));
		updateAccounts(populatedTransactions);		
		return saveTransactions(populatedTransactions);
	}

	@Override
	public List<Transaction> createPayments(List<Transaction> transactions) {
		validate(transactions);
		loadAccounts(transactions);
		List<Transaction> populatedTransactions = populeTransactions(OperationCategory.PAGAMENTO, transactions);
		updateAccounts(populatedTransactions);
		return saveTransactions(populatedTransactions);
	}

	private void validate(List<Transaction> transactions) {
		transactions.forEach(validator::basicValidations);
	}

	private void loadAccounts(List<Transaction> transactions) {
		transactions.forEach(item -> item.setAccount(accountService.findById(item.getAccount().getId())));
	}

	private List<Transaction> populeTransactions(OperationCategory operationCategory, List<Transaction> transactions) {
		return transactionOperationFactory.getTransactionOperation(operationCategory)
										  .populeTransactions(transactions);
	}

	private List<Transaction> saveTransactions(List<Transaction> populatedTransactions) {
		return transactionRepository.saveAll(populatedTransactions);
	}

	private void updateAccounts(List<Transaction> transactions) {
		accountService.updateAll(transactions.stream()
											 .map(Transaction::getAccount)
											 .distinct()
											 .collect(Collectors.toList()));
	}
}