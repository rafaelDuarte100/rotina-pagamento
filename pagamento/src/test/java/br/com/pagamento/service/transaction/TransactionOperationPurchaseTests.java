package br.com.pagamento.service.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.transaction.TransactionRepository;
import br.com.pagamento.util.account.AccountUtil;
import br.com.pagamento.validator.transaction.TransactionValidator;

@RunWith(MockitoJUnitRunner.class)
public class TransactionOperationPurchaseTests {

	@InjectMocks
	private TransactionOperationPurchase transactionOperationPurchase;
	
	@Mock
	private TransactionValidator validator;
	
	@Mock
	private TransactionRepository transactionRepository;
	
	@Mock
	private AccountUtil accountUtil;
	
	@Mock
	private TransactionValidator transactionValidator;
	
	private Account account;
	private Transaction creditPaymentTransaction;
	
	@Before
	public void setup() {
		account = Account.builder().id(1L).availableCreditLimit(500D).availableWithdrawalLimit(500D).build();
		creditPaymentTransaction = Transaction.builder()
											  .id(1L)
											  .amount(100D)
											  .balance(50D)
											  .account(account).build();
	}
	
	@Test
	public void testPurchaseTransactionWithoutPaymentCredit() {
		doReturn(null).when(transactionRepository).findCreditPaymentTransaction(any());
		Transaction transaction = Transaction.builder().account(account).amount(100D).build();
		
		
		Transaction transactionGenerated = transactionOperationPurchase.populeTransactions(transaction).get(0);
		
		assertThat(transactionGenerated.getAmount()).isEqualTo(-100D);
		assertThat(transactionGenerated.getBalance()).isEqualTo(-100D);
		assertThat(transactionGenerated.getAccount().getAvailableCreditLimit()).isEqualTo(400D);
		assertThat(transactionGenerated.getAccount().getAvailableWithdrawalLimit()).isEqualTo(500D);
	}
	
	@Test
	public void testPurchaseTransactionWithPaymentCredit() {
		doReturn(creditPaymentTransaction).when(transactionRepository).findCreditPaymentTransaction(any());
		Transaction transaction = Transaction.builder().account(account).amount(100D).build();
		
		List<Transaction> transactions = transactionOperationPurchase.populeTransactions(transaction);
		Transaction creditPaymentTransaction = transactions.get(0);
		Transaction transactionGenerated = transactions.get(1);
		
		assertThat(transactionGenerated.getAmount()).isEqualTo(-100D);
		assertThat(transactionGenerated.getBalance()).isEqualTo(-50D);
		
		assertThat(creditPaymentTransaction.getAmount()).isEqualTo(100D);
		assertThat(creditPaymentTransaction.getBalance()).isEqualTo(0D);
		
		assertThat(transactionGenerated.getAccount().getAvailableCreditLimit()).isEqualTo(450D);
		assertThat(transactionGenerated.getAccount().getAvailableWithdrawalLimit()).isEqualTo(500D);
	}
}
