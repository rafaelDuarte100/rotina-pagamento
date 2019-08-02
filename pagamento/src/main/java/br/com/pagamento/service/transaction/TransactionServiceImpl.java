package br.com.pagamento.service.transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.exception.TransactionNotFoundException;
import br.com.pagamento.model.transaction.OperationCategory;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.transaction.TransactionRepository;
import br.com.pagamento.service.account.AccountService;
import br.com.pagamento.validator.transaction.TransactionValidator;
import lombok.AllArgsConstructor;

@Service
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
	@Transactional
	public List<Transaction> create(Transaction transaction) {
		validate(transaction);
		loadAccount(transaction);
		List<Transaction> populatedTransactions = populeTransactions(transaction.getOperationType().getCategory(), transaction);
		updateAccounts(populatedTransactions);		
		return saveTransactions(populatedTransactions);
	}
	
	@Override
	public List<Transaction> createPayments(List<Transaction> transactions) {
		List<Transaction> transactionsGenerated = new ArrayList<>();
		for (Transaction transaction : transactions) {
			try {
				transactionsGenerated.addAll(create(transaction));
			} catch (ResourceException e) {}
		}
		return transactionsGenerated;
	}

	private void validate(Transaction transaction) {
		validator.basicValidations(transaction);
	}

	private void loadAccount(Transaction transaction) {
		transaction.setAccount(accountService.findById(transaction.getAccount().getId()));
	}

	private List<Transaction> populeTransactions(OperationCategory operationCategory, Transaction transaction) {
		return transactionOperationFactory.getTransactionOperation(operationCategory)
										  .populeTransactions(transaction);
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