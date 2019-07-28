package br.com.pagamento.transaction.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TransactionOperationFactory {

    private Map<Long, TransactionOperation> implementationByOperationType;

    public TransactionOperationFactory(List<TransactionOperation> implementations) {
        implementationByOperationType = new HashMap<>();
        implementations.forEach(implementation -> implementationByOperationType.put(implementation.getOperationType(), implementation));
    }

    public TransactionOperation getTransactionOperation(Long operationType) {
        return implementationByOperationType.get(operationType);
    }
}