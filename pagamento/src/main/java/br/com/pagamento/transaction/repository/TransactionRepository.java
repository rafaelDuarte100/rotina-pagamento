package br.com.pagamento.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.pagamento.transaction.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT COALESCE(SUM(t.balance), 0.0) FROM Transaction t JOIN t.account a WHERE a.id = ?1")
    public Double findTotalCreditByIdConta(Long id);

    @Query("FROM Transaction t " +
           "WHERE t.balance <> 0 AND t.account.id = ?1 " +
           "ORDER BY t.operationType.chargeOrder, t.eventDate ")
    public List<Transaction> findTransactionsToDownPayment(Long accountId);
}