package br.com.pagamento.service.transaction;

import java.util.ArrayList;
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
    public List<Transaction> populeTransactions(List<Transaction> transactions) {
        transactionsGenerated = new ArrayList<>();
        transactions.forEach(paymentTransaction -> populePayment(paymentTransaction));
        return getTransactionsGenerated();
    } 

    protected void populePayment(Transaction paymentTransaction) {
        Account account = paymentTransaction.getAccount();
        validate(paymentTransaction, account, null);

        Double paymentBalance = paymentTransaction.getAmount();
        List<Transaction> transactionsToDownPayment = transactionRepository.findTransactionsToDownPayment(account.getId());
        
        Double transactionRebate = 0D;
        for (Transaction transactionToDownPayment : transactionsToDownPayment) {
            transactionRebate = calculateTransactionRebate(transactionToDownPayment, paymentBalance);
            returnAvailableAccountLimite(account, transactionToDownPayment, transactionRebate);
            transactionToDownPayment.setBalance(transactionToDownPayment.getBalance() + transactionRebate);
            transactionsGenerated.add(transactionToDownPayment);
            paymentBalance -= transactionRebate;

            if (paymentBalance == 0) 
                break;
        }
        paymentTransaction.setBalance(paymentBalance);
        transactionsGenerated.add(paymentTransaction);
    }

    private Double calculateTransactionRebate(Transaction transactionToDownPayment, Double paymentBalance) {
        Double transactionBalance = transactionToDownPayment.getBalance();
        return Math.min(Math.abs(transactionBalance), paymentBalance);
    }

    private void returnAvailableAccountLimite(Account account, Transaction transactionToDownPayment, Double returnedValue) {
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
    protected void downAvailableLimitAccount(Transaction transaction, Account account) {}

	@Override
	protected void validate(Transaction transaction, Account account, Transaction positiveBalancePayment) {
		validator.validateTransactionPayment(transaction, account);		
	}
}