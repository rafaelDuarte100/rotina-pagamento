package br.com.pagamento.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pagamento.model.account.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}