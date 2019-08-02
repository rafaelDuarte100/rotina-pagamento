package br.com.pagamento.service.transaction;

import java.util.List;

import br.com.pagamento.model.transaction.OperationCategory;
import br.com.pagamento.model.transaction.Transaction;

public interface TransactionOperation {

    public List<Transaction> populeTransactions(Transaction transactions);
    public OperationCategory getOperationCategory();
}