package br.com.pagamento.exception;

import org.springframework.http.HttpStatus;

import br.com.pagamento.messages.ErrorMessage;
import lombok.Getter;

@Getter
public class ResourceException extends RuntimeException {

    private static final long serialVersionUID = 4025018719956394210L;
    
    private HttpStatus httpStatus;
    private ErrorMessage error;

    public ResourceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.error = new ErrorMessage(httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }
}