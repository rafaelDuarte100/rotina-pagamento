package br.com.pagamento.service.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.SpyBean;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationType;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.transaction.TransactionRepository;
import br.com.pagamento.service.account.AccountService;
import br.com.pagamento.service.account.AccountServiceImpl;
import br.com.pagamento.validator.transaction.TransactionValidator;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTests {

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountServiceImpl accountService;

    @SpyBean
    private TransactionValidator validator;
    

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ResourceException.class)
    public void testCreateTransactionWithAccountWasEntered() {
        Transaction transaction = new Transaction();
        transaction.setAccount(new Account());
        
        doThrow(ResourceException.class).when(validator).basicValidations(any());

        transactionServiceImpl.create(transaction);

        verify(accountService, never()).findById(any());
    }

    public void teste() {
        Account account = Account.builder().id(1L).availableCreditLimit(500D).availableWithdrawalLimit(500D).build();
        Transaction saque = Transaction.builder().id(1L).account(account).amount(50D).operationType(buildOperation(3L)).build();
        Transaction compra1 = Transaction.builder().id(2L).account(account).amount(20D).operationType(buildOperation(1L)).build();
        Transaction compra2 = Transaction.builder().id(3L).account(account).amount(40D).operationType(buildOperation(1L)).build();


    }

    public OperationType buildOperation(long type) {
        return OperationType.builder()
                            .id(type)
                            .build();
    }

}