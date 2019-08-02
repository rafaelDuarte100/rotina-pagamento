package br.com.pagamento.service.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.SpyBean;

import br.com.pagamento.exception.AccountNotFoundException;
import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.SourceMessage;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.repository.account.AccountRepository;
import br.com.pagamento.util.account.AccountUtil;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTests {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SourceMessage sourceMessage;

    @SpyBean
    private AccountUtil accountUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lenient().doReturn("Mensagem de erro retornada!").when(sourceMessage).getMessage(any());
    }

    @Test(expected = AccountNotFoundException.class)
    public void tryUpdateNonExistentAccount() {
    	lenient().doReturn(Optional.empty()).when(accountRepository).findById(any());

        accountService.updateLimits(Account.builder().id(1L).build());

        verify(accountRepository, times(1)).findById(any());
        verify(accountRepository, never()).save(any());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testFindByIdWithAccountEmpty() {
    	lenient().doReturn(Optional.empty()).when(accountRepository).findById(any());

        accountService.findById(-1);
    }

    @Test(expected = ResourceException.class)
    public void testUpdateLimitsWithCreditLimitLessThanZero() {
        Account account = Account.builder().id(1L).availableCreditLimit(-1D).build();
        lenient().doReturn(Optional.of(account)).when(accountRepository).findById(any());

        accountService.updateLimits(account);

        verify(accountRepository, never()).save(any());
        verify(accountUtil, times(1)).updateAccountLimits(any(), any());
    }

    @Test(expected = ResourceException.class)
    public void testUpdateLimitsWithWithdrawalLessThanZero() {
        Account account = Account.builder().id(1L).availableWithdrawalLimit(-1D).build();
        lenient().doReturn(Optional.of(account)).when(accountRepository).findById(any());

        accountService.updateLimits(account);

        verify(accountRepository, never()).save(any());
        verify(accountUtil, times(1)).updateAccountLimits(any(), any());
    }
}