package br.com.pagamento.validator.transaction;

import static org.mockito.Mockito.doReturn;

import org.junit.Before;
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
    
    private Transaction uninformedAccount;
    
    private Transaction informedAccount;
    
    private Transaction informedOperationType;
    
    private Transaction informedNegativeAmount;
    
    private Transaction amountGreateThanZero;
    
    private Transaction creditLimitNotNull;
    
    private Transaction suficientCreditLimit;
    
    @Before
    public void setup() {
    	uninformedAccount = Transaction.builder().account(new Account()).build();
    	
    	Account account = Account.builder().id(1L).build();
    	informedAccount = Transaction.builder().account(account).operationType(new OperationType()).build();
    	
    	OperationType operationType = OperationType.builder().id(1L).build();
    	informedOperationType = Transaction.builder().account(account).operationType(operationType).build();
    	
    	informedNegativeAmount = Transaction.builder().account(account).operationType(operationType).amount(-100D).build();
    	
    	amountGreateThanZero = Transaction.builder().account(account).operationType(operationType).amount(99D).build();
    	
    	Account accountWithCreditLimit = Account.builder().id(1L).availableCreditLimit(100D).availableWithdrawalLimit(100D).build();
    	creditLimitNotNull = Transaction.builder().account(accountWithCreditLimit).amount(200D).build();
    	
    	suficientCreditLimit = Transaction.builder().account(accountWithCreditLimit).amount(100D).build();
    	
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAccountNotEntered() {
    	transactionValidator.basicValidations(uninformedAccount);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAccountNotExists() {
    	doReturn(false).when(accountRepository).existsById(1L);
    	transactionValidator.basicValidations(informedAccount);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithOperationTypeNotEntered() {
    	doReturn(true).when(accountRepository).existsById(1L);
    	transactionValidator.basicValidations(informedAccount);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithOperationTypeNotExists() {
    	doReturn(true).when(accountRepository).existsById(1L);
    	doReturn(false).when(operationTypeRepository).existsById(1L);
    	transactionValidator.basicValidations(informedOperationType);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAmountNotEntered() {
    	doReturn(true).when(accountRepository).existsById(1L);
    	doReturn(true).when(operationTypeRepository).existsById(1L);
    	transactionValidator.basicValidations(informedOperationType);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAmountLessThanZero() {
    	doReturn(true).when(accountRepository).existsById(1L);
    	doReturn(true).when(operationTypeRepository).existsById(1L);
    	transactionValidator.basicValidations(informedNegativeAmount);
    }
    
    @Test
    public void testTransactionBasciValidation() {
    	doReturn(true).when(accountRepository).existsById(1L);
    	doReturn(true).when(operationTypeRepository).existsById(1L);
    	transactionValidator.basicValidations(amountGreateThanZero);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionCashPurchaseWithCreditLimitNull() {
    	transactionValidator.validateTransactionCashPurchase(amountGreateThanZero, 
    														 amountGreateThanZero.getAccount(), null);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionCashPurchaseWithInsuficientCreditLimit() {
    	transactionValidator.validateTransactionCashPurchase(creditLimitNotNull, 
				 											 creditLimitNotNull.getAccount(), null);    	
    }
    
    @Test
    public void testTransactionCashPurchaseWithSuficientCreditLimit() {
    	transactionValidator.validateTransactionCashPurchase(suficientCreditLimit, 
    														 suficientCreditLimit.getAccount(), null);    	
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithdrawWithWithdrawalLimitNull() {
    	transactionValidator.validateTransactionWithdraw(amountGreateThanZero, 
    													 amountGreateThanZero.getAccount(), null);
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionCashPurchaseWithInsuficientWithdrawLimit() {
    	transactionValidator.validateTransactionWithdraw(creditLimitNotNull, 
    													 creditLimitNotNull.getAccount(), null);
    }
    
    @Test
    public void testTransactionCashPurchaseWithSuficientWithdrawLimit() {
    	transactionValidator.validateTransactionWithdraw(suficientCreditLimit, 
    													 suficientCreditLimit.getAccount(), null);    	
    }
    
    @Test(expected = ResourceException.class)
    public void testTransactionPaymentWithNoOutStandingBalance() {
    	doReturn(false).when(accountRepository).thereIsOutstandingBalance(1L);
    	transactionValidator.validateTransactionPayment(suficientCreditLimit, suficientCreditLimit.getAccount());
    }
    
    @Test
    public void testTransactionPaymentWithOutStandingBalance() {
    	doReturn(true).when(accountRepository).thereIsOutstandingBalance(1L);
    	transactionValidator.validateTransactionPayment(suficientCreditLimit, suficientCreditLimit.getAccount());
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
    
    @Test(expected = ResourceException.class)
    public void testTransactionWithAccountNoOutstandingBalance() {
    	doReturn(false).when(accountRepository).thereIsOutstandingBalance(1L);
    	Account account = Account.builder().id(1L).build();
    	Transaction transaction = Transaction.builder().account(account).build();
    	transactionValidator.validateIfThereIsOutstandingBalance(transaction);
    }
}
