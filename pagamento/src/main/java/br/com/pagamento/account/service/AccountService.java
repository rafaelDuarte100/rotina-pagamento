package br.com.pagamento.account.service;

import java.util.List;
import java.util.Optional;

import br.com.pagamento.account.model.Account;

public interface AccountService {

    public List<Account> findAll();
    public Optional<Account> create(Account account);
    public void update(Account account);
    public Optional<Account> findById(long id);
}