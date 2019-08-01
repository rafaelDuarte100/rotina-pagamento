package br.com.pagamento.service.account;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.exception.AccountNotFoundException;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.repository.account.AccountRepository;
import br.com.pagamento.util.account.AccountUtil;
import br.com.pagamento.validator.account.AccountValidator;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountValidator accountValidator;
    private final AccountUtil accountUtil;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account create(Account account) {
    	accountValidator.validateAccountCreation(account);
        return accountRepository.save(account);
    }
    
	@Override
	public Account findById(long id) {
        return accountRepository.findById(id)
                                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public Account updateLimits(Account account) {
    	return Optional.of(findById(account.getId()))
                    .map(accountDB -> accountUtil.updateAccountLimits(accountDB, account))
                    .map(accountValidator::validateAccountUpdate)
                    .map(accountRepository::save)
                    .orElseThrow(() -> new AccountNotFoundException(account.getId()));
    }

    @Override
    public void updateAll(List<Account> accounts) {
        accountRepository.saveAll(accounts);
    }
}