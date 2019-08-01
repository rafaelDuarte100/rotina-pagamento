package br.com.pagamento.validator.transaction;

import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.SourceMessage;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationType;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.account.AccountRepository;
import br.com.pagamento.repository.transaction.OperationTypeRepository;

@RunWith(MockitoJUnitRunner.class)
public class TransactionValidatorTests {

    @InjectMocks
    private TransactionValidator transactionValidator;

    @Mock
    private SourceMessage messageSource;
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private OperationTypeRepository operationTypeRepository;
    
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAccountNotEntered() {
    	Transaction transaction = Transaction.builder().account(new Account()).build();
    	transactionValidator.validateIfAccountWasEntered(transaction);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAccountNotExists() {
    	doReturn(false).when(accountRepository).existsById(1L);
    	Transaction transaction = Transaction.builder().account(Account.builder().id(1L).build()).build();
    	transactionValidator.validateIfAccountExists(transaction);    	
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithOperationTypeWasNotEntered() {
    	Transaction transaction = Transaction.builder().operationType(new OperationType()).build();
    	transactionValidator.validateIfOperationTypeWasEntered(transaction);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithOperationTypeNotExists() {
    	doReturn(false).when(operationTypeRepository).existsById(1L);
    	Transaction transaction = Transaction.builder().operationType(OperationType.builder().id(1L).build()).build();
    	transactionValidator.validateIfOperationTypeExists(transaction);    	
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAmountWasNotEntered() {
    	Transaction transaction = Transaction.builder().build();
    	transactionValidator.validateIfAmountWasEntered(transaction);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAmountLessThanZero() {
    	Transaction transaction = Transaction.builder().amount(-1.0).build();
    	transactionValidator.validateIfAmountLessThanZero(transaction);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAccountWithCreditLimitNull() {
    	Account account = Account.builder().build();
    	transactionValidator.validateCreditLimitNonNull(account);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAccountCreditLimitLess() {
    	Transaction transaction = Transaction.builder().amount(100.0).build();
    	Account account = Account.builder().availableCreditLimit(99.0).build();
    	transactionValidator.validateAvailableCreditLimit(account, transaction, null);
    }
    
    @Test
    public void testTransactionWithAccountWithCreditLimitPayment() {
    	Transaction transaction = Transaction.builder().amount(100.0).build();
    	Account account = Account.builder().availableCreditLimit(47.0).build();
    	Transaction positiveBalancePayment = Transaction.builder().balance(60.).build();
    	transactionValidator.validateAvailableCreditLimit(account, transaction, positiveBalancePayment);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAccountWithWithdrawalLimitNull() {
    	Account account = Account.builder().build();
    	transactionValidator.validateWithdrawalLimitNonNull(account);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAccountWithdrawalLimitLess() {
    	Transaction transaction = Transaction.builder().amount(100.0).build();
    	Account account = Account.builder().availableWithdrawalLimit(99.0).build();
    	transactionValidator.validateAvailableWithdrawalLimit(account, transaction, null);
    }
    
    @Test
    public void testTransactionWithAccountWithNonWithdrawalLimitButWithCreditLimitPayment() {
    	Transaction transaction = Transaction.builder().amount(100.0).build();
    	Account account = Account.builder().availableWithdrawalLimit(47.0).build();
    	Transaction positiveBalancePayment = Transaction.builder().balance(60.).build();
    	transactionValidator.validateAvailableWithdrawalLimit(account, transaction, positiveBalancePayment);
    }
}
