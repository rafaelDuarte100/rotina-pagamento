package br.com.pagamento.service.account;

import java.util.List;
import java.util.Optional;

import br.com.pagamento.model.account.Account;

public interface AccountService {

    public List<Account> findAll();
    public Optional<Account> create(Account account);
    public Optional<Account> updateLimits(Account account);
    public Account update(Account account);
    public Optional<Account> findById(long id);
}