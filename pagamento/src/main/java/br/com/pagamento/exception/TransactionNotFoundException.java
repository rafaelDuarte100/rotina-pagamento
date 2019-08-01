package br.com.pagamento.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TransactionNotFoundException extends ResourceException {

    private static final long serialVersionUID = 1L;

    private Long idTransaction;

    public TransactionNotFoundException(Long idTransaction) {
        super(HttpStatus.NOT_FOUND, "transacao.nao.existente");
        this.idTransaction = idTransaction;
    }    
}