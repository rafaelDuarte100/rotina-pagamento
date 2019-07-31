package br.com.pagamento.service.transaction;

import java.util.List;
import java.util.Optional;

import br.com.pagamento.model.transaction.Transaction;

public interface TransactionService {

    public List<Transaction> findAll();

    public Optional<Transaction> findById(Long id);
    
    public Optional<Transaction> create(Transaction transaction);

    public List<Transaction> createPayments(List<Transaction> transactions);
}