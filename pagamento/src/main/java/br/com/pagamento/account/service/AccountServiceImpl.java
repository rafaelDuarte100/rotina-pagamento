package br.com.pagamento.account.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.account.model.Account;
import br.com.pagamento.account.repository.AccountRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final MessageSource messageSource;

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
        Optional<Account> accountBDOptional = findById(account.getId());
        if (accountBDOptional.isPresent()) {
            Account accountBD = accountBDOptional.get();
            accountBD.addAvailableCreditLimit(account.getAvailableCreditLimit());
            accountBD.addAvailableWithdrawalLimit(account.getAvailableWithdrawalLimit());
            accountRepository.save(accountBD);
        }
        else {
            throw new ResourceException(HttpStatus.NOT_FOUND, messageSource.getMessage("conta.nao.existente", new Object[]{account.getId()}, null));
        }
    }

	@Override
	public Optional<Account> findById(long id) {
		return accountRepository.findById(id);
	}
}