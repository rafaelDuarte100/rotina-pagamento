package br.com.pagamento.service.transaction;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationCategory;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.account.AccountRepository;
import br.com.pagamento.repository.transaction.TransactionRepository;
import br.com.pagamento.util.account.AccountUtil;
import lombok.AllArgsConstructor;

@Component
@Transactional
@AllArgsConstructor
public class TransactionOperationPayment implements TransactionOperation {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountUtil accountUtil;

    @Override
    public Transaction populeTransaction(Transaction transaction) {
        Account account = loadAccount(transaction);

        Double paymentBalance = transaction.getAmount();
        List<Transaction> transactionsToDownPayment = transactionRepository.findTransactionsToDownPayment(account.getId());
        
        if (transactionsToDownPayment != null) {
            for (Transaction transactionToDownPayment: transactionsToDownPayment) {
                if (paymentBalance <= 0) 
                    break;
                paymentBalance = downPaymentInTransactionBalance(transactionToDownPayment, account, paymentBalance);
            }
        }
        transaction.setBalance(paymentBalance);
        transaction.setAccount(account);
        return transaction;
    }

    public Double downPaymentInTransactionBalance(Transaction transactionToDownPayment, Account account, Double paymentBalance) {
        Double transactionBalance = transactionToDownPayment.getBalance();
        Double downValue = 0.0;

        if (transactionBalance > 0) {
            paymentBalance += transactionBalance;
            downValue -= transactionBalance;
        } else {
            downValue = Math.abs(transactionBalance) >= paymentBalance ? paymentBalance : Math.abs(transactionBalance);
            paymentBalance -= downValue;
            downPaymentInAvailableLimitAccount(account, transactionToDownPayment, downValue);
        }
        transactionToDownPayment.setBalance(transactionBalance + downValue);
        transactionRepository.save(transactionToDownPayment);
        return paymentBalance;
    }

    private void downPaymentInAvailableLimitAccount(Account account, Transaction transactionToDownPayment, Double downValue) {
        if (transactionToDownPayment.getOperationType().getCategory().equals(OperationCategory.SAQUE)) {
            account.setAvailableWithdrawalLimit(accountUtil.addAccountWithdrawalLimit(account, downValue));
        }
        else {
        	account.setAvailableCreditLimit(accountUtil.addAccountCreditLimit(account, downValue));
        }
    }

    private Account loadAccount(Transaction transaction) {
        return accountRepository.findById(transaction.getAccount().getId()).get();
    }

    @Override
    public OperationCategory getOperationCategory() {
		return OperationCategory.PAGAMENTO;
	}    
}