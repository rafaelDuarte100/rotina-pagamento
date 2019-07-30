package br.com.pagamento.service.account;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.SourceMessage;
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
    private final SourceMessage messageSource;
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
	public Optional<Account> findById(long id) {
		return accountRepository.findById(id);
    }

    @Override
    public Account update(Account account) {
    	return findById(account.getId())
                .map(accountDB -> { accountUtil.updateAccountLimits(accountDB, account.getAvailableCreditLimit(), account.getAvailableWithdrawalLimit());
                                    accountValidator.validateAccountUpdate(accountDB);
					    			return accountRepository.save(accountDB); })
		    	.orElseThrow(() -> new ResourceException(HttpStatus.NOT_FOUND, messageSource.getMessage("conta.nao.existente", account.getId(), null)));
    }
}