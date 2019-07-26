package br.com.pagamento.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pagamento.account.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}