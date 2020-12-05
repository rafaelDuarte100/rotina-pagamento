package br.com.pagamento.util.account;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.pagamento.messages.SourceMessage;
import br.com.pagamento.model.account.Account;

@RunWith(MockitoJUnitRunner.class)
public class AccountUtilTests {

    @InjectMocks
    private AccountUtil accountUtil;

    @Mock
    private SourceMessage messageSource;
    
    private Account accountWithAvailableLimitsNull;
    private Account accountWithAvailableLimitsNonNull;
    
    private Account newAccountWithAvailableLimitsNull;
    private Account newAccountWithAvailableLimitsNonNull;
    
    @Before
    public void setup() {
    	accountWithAvailableLimitsNull = new Account();
    	accountWithAvailableLimitsNonNull = Account.builder()
    											   .availableCreditLimit(100.65)
    											   .availableWithdrawalLimit(80.11)
    											   .build();
    	
    	newAccountWithAvailableLimitsNull = new Account();
    	newAccountWithAvailableLimitsNonNull = Account.builder()
	    											  .availableCreditLimit(99.37)
	    											  .availableWithdrawalLimit(77.55)
	    											  .build();
    }
    
    @Test
    public void testUpdateCreditLimitAccountWithNullCreditLimitWithNonNullValue() {
    	Account accountUpdated = accountUtil.updateAccountLimits(accountWithAvailableLimitsNull, newAccountWithAvailableLimitsNonNull);
    	assertThat(accountUpdated.getAvailableCreditLimit(), equalTo(newAccountWithAvailableLimitsNonNull.getAvailableCreditLimit()));
    }
    
    @Test
    public void testUpdateCreditLimitAccountWithNullCreditLimitWithNullValue() {
    	Account accountUpdated = accountUtil.updateAccountLimits(accountWithAvailableLimitsNull, newAccountWithAvailableLimitsNull);
    	assertThat(accountUpdated.getAvailableCreditLimit(), equalTo(null));
    }
    
    @Test
    public void testUpdateCreditLimitAccountWithNonNullCreditLimitWithNonNullValue() {
    	Account accountUpdated = accountUtil.updateAccountLimits(accountWithAvailableLimitsNonNull, newAccountWithAvailableLimitsNonNull);
    	assertThat(accountUpdated.getAvailableCreditLimit(), equalTo(newAccountWithAvailableLimitsNonNull.getAvailableCreditLimit()));
    }
    
    @Test
    public void testUpdateCreditLimitAccountWithNonNullCreditLimitWithNullValue() {
    	Account accountUpdated = accountUtil.updateAccountLimits(accountWithAvailableLimitsNonNull, newAccountWithAvailableLimitsNull);
    	assertThat(accountUpdated.getAvailableCreditLimit(), equalTo(accountWithAvailableLimitsNonNull.getAvailableCreditLimit()));
    }
    
    @Test
    public void testUpdateWithdrawalLimitAccountWithNullWithdrawalLimitWithNonNullValue() {
    	Account accountUpdated = accountUtil.updateAccountLimits(accountWithAvailableLimitsNull, newAccountWithAvailableLimitsNonNull);
    	assertThat(accountUpdated.getAvailableWithdrawalLimit(), equalTo(newAccountWithAvailableLimitsNonNull.getAvailableWithdrawalLimit()));
    }
    
    @Test
    public void testUpdateWithdrawalLimitAccountWithNullWithdrawalLimitWithNullValue() {
    	Account accountUpdated = accountUtil.updateAccountLimits(accountWithAvailableLimitsNull, newAccountWithAvailableLimitsNull);
    	assertThat(accountUpdated.getAvailableWithdrawalLimit(), equalTo(null));
    }
    
    @Test
    public void testUpdateWithdrawalLimitAccountWithNonNullWithdrawalLimitWithNonNullValue() {
    	Account accountUpdated = accountUtil.updateAccountLimits(accountWithAvailableLimitsNonNull, newAccountWithAvailableLimitsNonNull);
    	assertThat(accountUpdated.getAvailableWithdrawalLimit(), equalTo(newAccountWithAvailableLimitsNonNull.getAvailableWithdrawalLimit()));
    }
    
    @Test
    public void testUpdateWithdrawalLimitAccountWithNonNullWithdrawalLimitWithNullValue() {
    	Account accountUpdated = accountUtil.updateAccountLimits(accountWithAvailableLimitsNonNull, newAccountWithAvailableLimitsNull);
    	assertThat(accountUpdated.getAvailableWithdrawalLimit(), equalTo(accountWithAvailableLimitsNonNull.getAvailableWithdrawalLimit()));
    }
}
