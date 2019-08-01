package br.com.pagamento.service.transaction;

import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;

import org.springframework.stereotype.Component;

import br.com.pagamento.model.transaction.OperationCategory;

@Component
public class TransactionOperationFactory {

    private Map<OperationCategory, TransactionOperation> implementationByOperationCategory;

    public TransactionOperationFactory(List<TransactionOperation> implementations) {
        implementationByOperationCategory = implementations.stream()
                                                           .collect(toMap(TransactionOperation::getOperationCategory, impl -> impl));
    }

    public TransactionOperation getTransactionOperation(OperationCategory operationCategory) {
        return implementationByOperationCategory.get(operationCategory);
    }
}