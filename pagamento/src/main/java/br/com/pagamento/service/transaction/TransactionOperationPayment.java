package br.com.pagamento.service.transaction;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationCategory;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.transaction.TransactionRepository;
import br.com.pagamento.util.account.AccountUtil;
import br.com.pagamento.validator.transaction.TransactionValidator;

@Component
public class TransactionOperationPayment extends AbstractTransactionOperation {


    public TransactionOperationPayment(TransactionValidator validator, TransactionRepository transactionRepository,
                                        List<Transaction> resultado, AccountUtil accountUtil) {
        super(validator, transactionRepository, resultado, accountUtil);
    }

    @Override
    public List<Transaction> populeTransactions(Transaction... transactions) {
        Arrays.asList(transactions)
              .stream()
              .forEach(paymentTransaction -> populePayment(paymentTransaction));
        return resultado;
    } 

    public void populePayment(Transaction paymentTransaction) {
        Account account = paymentTransaction.getAccount();
        validator.validateTransactionPayment(paymentTransaction, account);

        Double paymentBalance = paymentTransaction.getAmount();
        List<Transaction> transactionsToDownPayment = transactionRepository.findTransactionsToDownPayment(account.getId());
        
        Double transactionRebate = 0D;
        for (Transaction transactionToDownPayment : transactionsToDownPayment) {
            transactionRebate = calculateTransactionRebate(transactionToDownPayment, paymentBalance);
            returnAvailableAccountLimite(account, transactionToDownPayment, transactionRebate);
            transactionToDownPayment.setBalance(transactionToDownPayment.getBalance() + transactionRebate);
            resultado.add(transactionToDownPayment);
            paymentBalance -= transactionRebate;

            if (paymentBalance == 0) 
                break;
        }
        paymentTransaction.setBalance(paymentBalance);
        resultado.add(paymentTransaction);
    }

    public Double calculateTransactionRebate(Transaction transactionToDownPayment, Double paymentBalance) {
        Double transactionBalance = transactionToDownPayment.getBalance();
        return Math.min(Math.abs(transactionBalance), paymentBalance);
    }

    public void returnAvailableAccountLimite(Account account, Transaction transactionToDownPayment, Double returnedValue) {
        if (transactionToDownPayment.getOperationType().getCategory().equals(OperationCategory.SAQUE)) {
            account.setAvailableWithdrawalLimit(accountUtil.addAccountWithdrawalLimit(account, returnedValue));
        }
        else {
        	account.setAvailableCreditLimit(accountUtil.addAccountCreditLimit(account, returnedValue));
        }
    }

    @Override
    public OperationCategory getOperationCategory() {
		return OperationCategory.PAGAMENTO;
	}

    @Override
    public void downAvailableLimitAccount(Transaction transaction, Account account) {}
}