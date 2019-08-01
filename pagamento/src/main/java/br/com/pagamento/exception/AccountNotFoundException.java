package br.com.pagamento.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AccountNotFoundException extends ResourceException {

    private static final long serialVersionUID = -7295630133672450844L;

    private Long idAccount;

    public AccountNotFoundException(Long idAccount) {
        super(HttpStatus.NOT_FOUND, "conta.nao.existente");
        this.idAccount = idAccount;
    }
}