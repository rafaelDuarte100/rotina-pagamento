package br.com.pagamento.transaction.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.account.repository.AccountRepository;
import br.com.pagamento.transaction.model.Transaction;
import br.com.pagamento.transaction.repository.TransactionRepository;
import br.com.pagamento.transaction.validator.TransactionValidator;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final TransactionOperationFactory transactionOperationFactory;
	private final AccountRepository accountRepository;
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
		return Optional.ofNullable(persistTransaction(transaction));
	}

	@Override
	public Optional<List<Transaction>> create(List<Transaction> transactions) {
		return Optional.ofNullable(transactions.stream()
											   .map(transaction -> persistTransaction(transaction))
											   .collect(Collectors.toList()));
	}

	private Transaction persistTransaction(Transaction transaction) {
		validator.basicValidations(transaction);
		transaction = transactionOperationFactory.getTransactionOperation(transaction.getOperationType().getCategory())
												 .populeTransaction(transaction);
		accountRepository.save(transaction.getAccount());
		return transactionRepository.save(transaction);
	}
}