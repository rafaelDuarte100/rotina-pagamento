package br.com.pagamento.repository.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pagamento.model.transaction.OperationType;
import br.com.pagamento.model.transaction.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("FROM Transaction t " + 
           "WHERE t.operationType.id = " + OperationType.PAGAMENTO + 
           "AND t.balance > 0 " +
           "AND t.account.id = :accountId ")
    public Transaction findCreditPaymentTransaction(@Param("accountId") Long accountId);

    @Query("FROM Transaction t " +
           "WHERE t.balance < 0 AND t.account.id = :accountId " +
           "ORDER BY t.operationType.chargeOrder, t.eventDate ")
    public List<Transaction> findTransactionsToDownPayment(@Param("accountId") Long accountId);
}