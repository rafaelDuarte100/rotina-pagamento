package br.com.pagamento.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pagamento.transaction.model.OperationType;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {
    
}