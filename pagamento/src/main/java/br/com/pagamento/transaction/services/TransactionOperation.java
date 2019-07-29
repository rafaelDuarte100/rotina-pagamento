package br.com.pagamento.transaction.services;

import br.com.pagamento.transaction.model.OperationCategory;
import br.com.pagamento.transaction.model.Transaction;

public interface TransactionOperation {

    public Transaction populeTransaction(Transaction transaction);
    public OperationCategory getOperationCategory();
}