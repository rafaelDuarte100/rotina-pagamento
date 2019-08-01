package br.com.pagamento.service.transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.OperationCategory;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.transaction.TransactionRepository;
import br.com.pagamento.util.account.AccountUtil;
import br.com.pagamento.validator.transaction.TransactionValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractTransactionOperation implements TransactionOperation {

    protected final TransactionValidator validator;
    protected final TransactionRepository transactionRepository;

    @Getter
    protected List<Transaction> transactionsGenerated;
    protected final AccountUtil accountUtil;

    public List<Transaction> populeTransactions(List<Transaction> transactions) {
        transactionsGenerated = new ArrayList<>();
        Transaction transaction = transactions.get(0);
        Account account = transaction.getAccount();
        Transaction creditPaymentTransaction = findCreditPaymentTransaction(account);
        validator.validateTransactionCashPurchase(transaction, account, creditPaymentTransaction);
        
        Double creditBalance = null;
        if (creditPaymentTransaction != null) {
            creditBalance = creditPaymentTransaction.getBalance();
            creditPaymentTransaction.setBalance(calculateNewBalanceToCreditPaymentTransaction(transaction, creditBalance));
            transactionsGenerated.add(creditPaymentTransaction);
        }
        transaction.setBalance(inverterSignal(calculateTransactionBalance(transaction, creditBalance)));
        transaction.setAmount(inverterSignal(transaction.getAmount()));
        downAvailableLimitAccount(transaction, account);
        transactionsGenerated.add(transaction);
        
        return transactionsGenerated;
    }

    protected Double calculateNewBalanceToCreditPaymentTransaction(Transaction transaction, Double creditBalance) {
        return Math.max(creditBalance - transaction.getAmount(), 0);
    }

    protected Double calculateTransactionBalance(Transaction transaction, Double creditBalance) {
        return Optional.ofNullable(creditBalance)
                       .map(credit -> Math.max(transaction.getAmount() - credit, 0))
                       .orElse(Math.abs(transaction.getAmount()));
    }

    private Transaction findCreditPaymentTransaction(Account account) {
        return transactionRepository.findCreditPaymentTransaction(account.getId());
    }

    protected Double inverterSignal(Double value) {
        return truncateValue(value) * (-1);
    }

    private Double truncateValue(Double value) {
		return Double.valueOf(new DecimalFormat("#.##").format(value).replace(",", "."));
	}

    protected abstract void downAvailableLimitAccount(Transaction transaction, Account account);

    public abstract OperationCategory getOperationCategory();
}