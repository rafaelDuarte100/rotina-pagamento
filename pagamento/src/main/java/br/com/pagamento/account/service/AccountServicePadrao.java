package br.com.pagamento.account.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.pagamento.account.model.Account;
import br.com.pagamento.account.repository.AccountRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServicePadrao implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> create(Account account) {
        return Optional.of(accountRepository.save(account));
    }

    @Override
    public void update(Account account) {
        accountRepository.save(account);
    }

	@Override
	public Optional<Account> findById(long id) {
		return accountRepository.findById(id);
	}
}