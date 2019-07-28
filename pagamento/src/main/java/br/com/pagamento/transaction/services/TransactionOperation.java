package br.com.pagamento.transaction.services;

import java.util.Optional;

import br.com.pagamento.transaction.model.Transaction;

public interface TransactionOperation {

    public Optional<Transaction> insertTransaction(Transaction transaction);
    public Long getOperationType();
}