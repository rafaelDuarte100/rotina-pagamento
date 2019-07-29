package br.com.pagamento.account.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.account.model.Account;
import br.com.pagamento.account.repository.AccountRepository;
import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.SourceMessage;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final SourceMessage messageSource;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> create(Account account) {
        return Optional.ofNullable(accountRepository.save(account));
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
            throw new ResourceException(HttpStatus.NOT_FOUND, messageSource.getMessage("conta.nao.existente", account.getId(), null));
        }
    }

	@Override
	public Optional<Account> findById(long id) {
		return accountRepository.findById(id);
    }
}