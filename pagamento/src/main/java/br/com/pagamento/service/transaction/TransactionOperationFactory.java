package br.com.pagamento.service.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.pagamento.model.transaction.OperationCategory;

@Component
public class TransactionOperationFactory {

    private Map<OperationCategory, TransactionOperation> implementationByOperationCategory;

    public TransactionOperationFactory(List<TransactionOperation> implementations) {
        implementationByOperationCategory = new HashMap<>();
        implementations.forEach(implementation -> implementationByOperationCategory.put(implementation.getOperationCategory(), implementation));
    }

    public TransactionOperation getTransactionOperation(OperationCategory operationCategory) {
        return implementationByOperationCategory.get(operationCategory);
    }
}