package br.com.pagamento.validator.transaction;

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

    public void validateTransactionCashPurchase(Transaction transaction, Account account) {
        validateCreditLimitNonNull(account);
        validateAvailableCreditLimit(account, transaction);
    }

    public void validateTransactionWithdraw(Transaction transaction, Account account) {
        validateWithdrawalLimitNonNull(account);
        validateAvailableWithdrawalLimit(account, transaction);
    }

    private void validateIfAccountWasEntered(Transaction transaction) {
        if (transaction.getAccount().getId() == null)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.nao.informada"));
    }

    private void validateIfAccountExists(Transaction transaction) {
        Long id = transaction.getAccount().getId();
        if (!accountRepository.existsById(id))
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.nao.existente", id));
    }

    private void validateIfOperationTypeWasEntered(Transaction transaction) {
        if (transaction.getOperationType().getId() == null)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("tipo.operacao.nao.informado"));
    }

    private void validateIfOperationTypeExists(Transaction transaction) {
        Long id = transaction.getOperationType().getId();
        if (!operationTypeRepository.existsById(id))
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("tipo.operacao.nao.existente", id));
    }

    private void validateIfAmountWasEntered(Transaction transaction) {
        if (transaction.getAmount() == null)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("montante.nao.informado"));
    }

    private void validateIfAmountLessThanZero(Transaction transaction) {
        if (transaction.getAmount() <= 0)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("transacao.montante.negativo"));
    }

    private void validateCreditLimitNonNull(Account account) {
        if (account.getAvailableCreditLimit() == null)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.limite.credito.nao.cadastrado"));
    }

    private void validateWithdrawalLimitNonNull(Account account) {
        if (account.getAvailableWithdrawalLimit() == null)
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.limite.saque.nao.cadastrado"));
    }

    private void validateAvailableCreditLimit(Account account, Transaction transaction) {
        if (account.getAvailableCreditLimit() < transaction.getAmount())
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.limite.credito.insuficiente"));
    }

    private void validateAvailableWithdrawalLimit(Account account, Transaction transaction) {
        if (account.getAvailableWithdrawalLimit() < transaction.getAmount())
            throw new ResourceException(HttpStatus.NOT_ACCEPTABLE, messageSource.getMessage("conta.limite.saque.insuficiente"));
    }
}