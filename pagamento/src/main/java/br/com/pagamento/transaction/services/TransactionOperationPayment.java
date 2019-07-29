package br.com.pagamento.transaction.services;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.account.model.Account;
import br.com.pagamento.account.repository.AccountRepository;
import br.com.pagamento.transaction.model.OperationCategory;
import br.com.pagamento.transaction.model.Transaction;
import br.com.pagamento.transaction.repository.TransactionRepository;
import lombok.AllArgsConstructor;

@Component
@Transactional
@AllArgsConstructor
public class TransactionOperationPayment implements TransactionOperation {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

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
            account.addAvailableWithdrawalLimit(downValue);
        }
        else {
            account.addAvailableCreditLimit(downValue);
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