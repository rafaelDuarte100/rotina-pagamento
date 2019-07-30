package br.com.pagamento.validator.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.SourceMessage;
import br.com.pagamento.model.account.Account;

@RunWith(MockitoJUnitRunner.class)
public class AccountValidatorTests {

    @InjectMocks
    private AccountValidator accountValidator;

    @Mock
    private SourceMessage messageSource;

    @Test(expected = ResourceException.class)
    public void createAccountWithAvailableCreditLimitLessThanZero() {
        accountValidator.validateAccountCreation(Account.builder().availableCreditLimit(-1D).build());
    }

    @Test(expected = ResourceException.class)
    public void createAccountWithAvailableWithdrawalLimitLessThanZero() {
        accountValidator.validateAccountCreation(Account.builder().availableWithdrawalLimit(-1D).build());
    }

    @Test(expected = ResourceException.class)
    public void updateAccountWithAvailableCreditLimitLessThanZero() {
        accountValidator.validateAccountUpdate(Account.builder().availableCreditLimit(-1D).build());
    }

    @Test(expected = ResourceException.class)
    public void updateAccountWithAvailableWithdrawalLimitLessThanZero() {
        accountValidator.validateAccountUpdate(Account.builder().availableWithdrawalLimit(-1D).build());
    }
}