package br.com.pagamento.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pagamento.model.transaction.OperationType;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {
    
}