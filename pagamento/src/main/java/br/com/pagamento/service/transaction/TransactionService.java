package br.com.pagamento.service.transaction;

import java.util.List;

import br.com.pagamento.model.transaction.Transaction;

public interface TransactionService {

    public List<Transaction> findAll();

    public Transaction findById(Long id);
    
    public List<Transaction> create(Transaction transaction);

    public List<Transaction> createPayments(List<Transaction> transactions);
}