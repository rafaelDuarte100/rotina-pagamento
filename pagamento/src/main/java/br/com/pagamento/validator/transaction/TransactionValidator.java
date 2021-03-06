package br.com.pagamento.validator.transaction;

import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.SourceMessage;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.model.transaction.Transaction;
import br.com.pagamento.repository.account.AccountRepository;
import br.com.pagamento.repository.transaction.OperationTypeRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionValidator {

    private final AccountRepository accountRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final SourceMessage messageSource;

    public void basicValidations(Transaction transaction) {
        validateIfAccountWasEntered(transaction);
        validateIfAccountExists(transaction);
        validateIfOperationTypeWasEntered(transaction);
        validateIfOperationTypeExists(transaction);
        validateIfAmountWasEntered(transaction);
        validateIfAmountLessThanZero(transaction);
    }

    public void validateTransactionCashPurchase(Transaction transaction, Account account, Transaction positiveBalancePayment) {
        validateCreditLimitNonNull(account);
        validateAvailableCreditLimit(account, transaction, positiveBalancePayment);
    }

    public void validateTransactionWithdraw(Transaction transaction, Account account, Transaction positiveBalancePayment) {
        validateWithdrawalLimitNonNull(account);
        validateAvailableWithdrawalLimit(account, transaction, positiveBalancePayment);
    }

    public void validateTransactionPayment(Transaction transaction, Account account) {
        validateIfThereIsOutstandingBalance(transaction);
    }

    public void validateIfThereIsOutstandingBalance(Transaction transaction) {
        if (!accountRepository.thereIsOutstandingBalance(transaction.getAccount().getId())) {
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.sem.saldo.devedor", transaction.getAccount().getId()));
        }
    }

    public void validateIfAccountWasEntered(Transaction transaction) {
        if (Objects.isNull(transaction.getAccount().getId()))
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.nao.informada"));
    }

    public void validateIfAccountExists(Transaction transaction) {
        Long id = transaction.getAccount().getId();
        if (!accountRepository.existsById(id))
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.nao.existente", id));
    }

    public void validateIfOperationTypeWasEntered(Transaction transaction) {
        if (transaction.getOperationType().getId() == null)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("tipo.operacao.nao.informado"));
    }

    public void validateIfOperationTypeExists(Transaction transaction) {
        Long id = transaction.getOperationType().getId();
        if (!operationTypeRepository.existsById(id))
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("tipo.operacao.nao.existente", id));
    }

    public void validateIfAmountWasEntered(Transaction transaction) {
        if (transaction.getAmount() == null)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("montante.nao.informado"));
    }

    public void validateIfAmountLessThanZero(Transaction transaction) {
        if (transaction.getAmount() <= 0)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("transacao.montante.negativo"));
    }

    public void validateCreditLimitNonNull(Account account) {
        if (account.getAvailableCreditLimit() == null)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.limite.credito.nao.cadastrado"));
    }

    public void validateWithdrawalLimitNonNull(Account account) {
        if (account.getAvailableWithdrawalLimit() == null)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.limite.saque.nao.cadastrado"));
    }

    public void validateAvailableCreditLimit(Account account, Transaction transaction, Transaction positiveBalancePayment) {
        Double positiveBalance = Optional.ofNullable(positiveBalancePayment)
                                         .map(item -> item.getBalance())
                                         .orElse(0D);

        if ((account.getAvailableCreditLimit() + positiveBalance) < transaction.getAmount())
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.limite.credito.insuficiente"));
    }

    public void validateAvailableWithdrawalLimit(Account account, Transaction transaction, Transaction positiveBalancePayment) {
    	Double positiveBalance = Optional.ofNullable(positiveBalancePayment)
						                .map(item -> item.getBalance())
						                .orElse(0D);
    	
        if ((account.getAvailableWithdrawalLimit() + positiveBalance) < transaction.getAmount())
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.limite.saque.insuficiente"));
    }
}