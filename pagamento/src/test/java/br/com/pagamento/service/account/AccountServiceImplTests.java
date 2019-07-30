package br.com.pagamento.service.account;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.SourceMessage;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.repository.account.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTests {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SourceMessage sourceMessage;

    @Before
    public void init() {
        when(sourceMessage.getMessage(anyString())).thenReturn("Mensagem de erro retornada!");
    }

    @Test(expected = ResourceException.class)
    public void TryUpdateNonExistentAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        accountService.update(Account.builder().id(1L).build());
    }    
}