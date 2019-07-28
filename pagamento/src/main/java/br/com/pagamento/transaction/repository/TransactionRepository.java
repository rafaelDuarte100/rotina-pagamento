package br.com.pagamento.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.pagamento.transaction.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT COALESCE(SUM(t.balance), 0.0) FROM Transaction t JOIN t.account a WHERE a.id = ?1")
    public Double findTotalCreditByIdConta(Long id);
}