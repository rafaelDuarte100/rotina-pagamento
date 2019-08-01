package br.com.pagamento.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ResourceException extends RuntimeException {

    private static final long serialVersionUID = 4025018719956394210L;
    
    private HttpStatus httpStatus;
    private String messageKey;

    public ResourceException(HttpStatus httpStatus, String messageKey) {
        super(messageKey);
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}