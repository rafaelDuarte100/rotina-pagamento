package br.com.pagamento.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.ErrorMessage;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ResourceException.class })
    protected ResponseEntity<ErrorMessage> handleException(ResourceException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getError());
    }
}