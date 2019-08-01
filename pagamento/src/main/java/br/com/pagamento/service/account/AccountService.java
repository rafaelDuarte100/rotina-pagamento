package br.com.pagamento.service.account;

import java.util.List;

import br.com.pagamento.model.account.Account;

public interface AccountService {

    public List<Account> findAll();
    public Account create(Account account);
    public Account updateLimits(Account account);
    public Account findById(long id);
    public void updateAll(List<Account> accounts);
}