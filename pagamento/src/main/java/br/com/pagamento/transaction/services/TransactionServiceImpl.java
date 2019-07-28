package br.com.pagamento.transaction.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		validator.basicValidations(transaction);
		return transactionOperationFactory.getTransactionOperation(transaction.getOperationType().getId())
										  .insertTransaction(transaction);
	}
}