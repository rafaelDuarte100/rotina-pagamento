package br.com.pagamento.service.transaction;

import br.com.pagamento.model.transaction.OperationCategory;
import br.com.pagamento.model.transaction.Transaction;

public interface TransactionOperation {

    public Transaction populeTransaction(Transaction transaction);
    public OperationCategory getOperationCategory();
}