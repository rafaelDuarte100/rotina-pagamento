package br.com.pagamento.service.transaction;

import java.util.List;

import br.com.pagamento.model.transaction.Transaction;

public interface TransactionService {

    List<Transaction> findAll();

    Transaction findById(Long id);
    
    List<Transaction> create(Transaction transaction);

    List<Transaction> createPayments(List<Transaction> transactions);

    List<Transaction> findAllByAccount(Long accountId);
}