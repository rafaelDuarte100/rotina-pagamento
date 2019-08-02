package br.com.pagamento.validator.account;

import org.junit.Before;
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
    
    private Account availableCreditLimitLessThanZero;
    
    private Account availableCreditLimitGreaterThanZero;
    
    private Account availableWithdrawalLimitLessThanZero;
    
    private Account availableWithdrawalLimitGreaterThanZero;
    
    @Before
    public void setup() {
    	availableCreditLimitLessThanZero = Account.builder().availableCreditLimit(-1D).build();
    	availableCreditLimitGreaterThanZero = Account.builder().availableCreditLimit(1D).build();
    	
    	availableWithdrawalLimitLessThanZero = Account.builder().availableWithdrawalLimit(-1D).build();
    	availableWithdrawalLimitGreaterThanZero = Account.builder().availableWithdrawalLimit(1D).build();
    }

    @Test(expected = ResourceException.class)
    public void createAccountWithAvailableCreditLimitLessThanZero() {
        accountValidator.validateAccountCreation(availableCreditLimitLessThanZero);
    }
    
    @Test
    public void createAccountWithAvailableCreditLimitGreaterThanZero() {
        accountValidator.validateAccountCreation(availableCreditLimitGreaterThanZero);
    }

    @Test(expected = ResourceException.class)
    public void createAccountWithAvailableWithdrawalLimitLessThanZero() {
        accountValidator.validateAccountCreation(availableWithdrawalLimitLessThanZero);
    }
    
    @Test
    public void createAccountWithAvailableWithdrawalLimitGreaterThanZero() {
        accountValidator.validateAccountCreation(availableWithdrawalLimitGreaterThanZero);
    }

    @Test(expected = ResourceException.class)
    public void updateAccountWithAvailableCreditLimitLessThanZero() {
        accountValidator.validateAccountUpdate(availableCreditLimitLessThanZero);
    }
    
    @Test
    public void updateAccountWithAvailableCreditLimitGreaterThanZero() {
        accountValidator.validateAccountUpdate(availableCreditLimitGreaterThanZero);
    }

    @Test(expected = ResourceException.class)
    public void updateAccountWithAvailableWithdrawalLimitLessThanZero() {
        accountValidator.validateAccountUpdate(availableWithdrawalLimitLessThanZero);
    }
    
    @Test
    public void updateAccountWithAvailableWithdrawalLimitGreaterThanZero() {
        accountValidator.validateAccountUpdate(availableWithdrawalLimitGreaterThanZero);
    }

}