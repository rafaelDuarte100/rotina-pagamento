package br.com.pagamento.service.transaction;

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

@AllArgsConstructor
public abstract class AbstractTransactionOperation implements TransactionOperation {

    protected final TransactionValidator validator;
    protected final TransactionRepository transactionRepository;
    protected List<Transaction> resultado;
    protected final AccountUtil accountUtil;

    public List<Transaction> populeTransactions(Transaction... transactions) {
        resultado = new ArrayList<>();
        Transaction transaction = transactions[0];
        Account account = transaction.getAccount();
        Transaction creditPaymentTransaction = findCreditPaymentTransaction(account);
        validator.validateTransactionCashPurchase(transaction, account, creditPaymentTransaction);
        
        Double creditBalance = null;
        if (creditPaymentTransaction != null) {
            creditBalance = creditPaymentTransaction.getBalance();
            creditPaymentTransaction.setBalance(calculateNewBalanceToCreditPaymentTransaction(transaction, creditBalance));
            resultado.add(creditPaymentTransaction);
        }
        transaction.setAmount(inverterSignal(transaction.getAmount()));
        transaction.setBalance(inverterSignal(calculateTransactionBalance(transaction, creditBalance)));
        downAvailableLimitAccount(transaction, account);
        resultado.add(transaction);
        
        return resultado;
    }

    public Double calculateNewBalanceToCreditPaymentTransaction(Transaction transaction, Double creditBalance) {
        return Math.max(creditBalance - transaction.getAmount(), 0);
    }

    public Double calculateTransactionBalance(Transaction transaction, Double creditBalance) {
        return Optional.ofNullable(creditBalance)
                       .map(credit -> Math.max(transaction.getAmount() - credit, 0))
                       .orElse(Math.abs(transaction.getAmount()));
    }

    private Transaction findCreditPaymentTransaction(Account account) {
        return transactionRepository.findCreditPaymentTransaction(account.getId());
    }

    public Double inverterSignal(Double value) {
        return value * (-1);
    }

    public abstract void downAvailableLimitAccount(Transaction transaction, Account account);

    public abstract OperationCategory getOperationCategory();
}