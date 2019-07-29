package br.com.pagamento.transaction.services;

import java.util.List;
import java.util.Optional;

import br.com.pagamento.transaction.model.Transaction;

public interface TransactionService {

    public List<Transaction> findAll();

    public Optional<Transaction> findById(Long id);
    
    public Optional<Transaction> create(Transaction account);

    public Optional<List<Transaction>> create(List<Transaction> account);
}