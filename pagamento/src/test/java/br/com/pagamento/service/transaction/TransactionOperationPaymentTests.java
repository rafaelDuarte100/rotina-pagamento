package br.com.pagamento.service.transaction;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationType;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.account.AccountRepository;
import br.com.pagamento.repository.transaction.TransactionRepository;
import br.com.pagamento.util.account.AccountUtil;
import br.com.pagamento.validator.transaction.TransactionValidator;

@RunWith(MockitoJUnitRunner.class)
public class TransactionOperationPaymentTests {

	@InjectMocks
	private TransactionOperationPayment transactionOperationPayment;
	
	@Mock
	private TransactionValidator validator;
	
	@Mock
	private TransactionRepository transactionRepository;
	
	@Spy
	private AccountUtil accountUtil;
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private TransactionValidator transactionValidator;
	
	private Account account;
	private Transaction saque;
	private Transaction compraAVista1;
	
	@Before
	public void setup() {
		
		account = Account.builder().id(1L).availableCreditLimit(476.50).availableWithdrawalLimit(450D).build();
		
		saque = Transaction.builder()
						   .id(1L)
						   .account(account)
						   .operationType(OperationType.builder().id(OperationType.SAQUE).build())
						   .amount(-50D)
						   .balance(-50D).build();
		compraAVista1 = Transaction.builder()
								   .id(2L)
								   .account(account)
								   .operationType(OperationType.builder().id(OperationType.COMPRA_A_VISTA).build())
								   .amount(-23.5)
								   .balance(-23.5).build();
	}
	
	@Test
	public void testTransactionPayment() {
		doReturn(asList(saque, compraAVista1)).when(transactionRepository).findTransactionsToDownPayment(any());
		Transaction pagamento = Transaction.builder().account(account).amount(70D).build();
		
		List<Transaction> transactions = transactionOperationPayment.populeTransactions(pagamento);
		saque = transactions.get(0);
		compraAVista1 = transactions.get(1);
		pagamento = transactions.get(2);
		
		Assertions.assertThat(saque.getAmount()).isEqualTo(-50D);
		Assertions.assertThat(saque.getBalance()).isEqualTo(0D);
		
		Assertions.assertThat(compraAVista1.getAmount()).isEqualTo(-23.5);
		Assertions.assertThat(compraAVista1.getBalance()).isEqualTo(-3.5D);
				
		Assertions.assertThat(account.getAvailableWithdrawalLimit()).isEqualTo(500D);
		Assertions.assertThat(account.getAvailableCreditLimit()).isEqualTo(496.5);		
	}
}
