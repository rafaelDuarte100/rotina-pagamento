package br.com.pagamento.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pagamento.model.account.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM transaction WHERE balance < 0 AND account_id = :accountId ) ",
           nativeQuery = true)
    public boolean thereIsOutstandingBalance(@Param("accountId") Long accountId);
}