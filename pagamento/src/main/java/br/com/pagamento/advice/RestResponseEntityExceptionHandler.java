package br.com.pagamento.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.pagamento.exception.AccountNotFoundException;
import br.com.pagamento.exception.ResourceException;
import br.com.pagamento.messages.ErrorMessage;
import br.com.pagamento.messages.SourceMessage;
import lombok.AllArgsConstructor;

@ControllerAdvice
@AllArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final SourceMessage messageSource;

    @ExceptionHandler(value = { ResourceException.class })
    protected ResponseEntity<ErrorMessage> handleResourceException(ResourceException e) {
        return generateResponse(e);
    }

    @ExceptionHandler(value = { AccountNotFoundException.class })
    protected ResponseEntity<ErrorMessage> handleAccountNotFoundException(AccountNotFoundException e) {
        return generateResponse(e, e.getIdAccount());
    }
    
    @ExceptionHandler(value = { Exception.class })
    private ResponseEntity<ErrorMessage> handleException(Exception e) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                                                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                .codeDescription(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                                .message(messageSource.getMessage("erro.nao.esperado"))
                                                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    private ResponseEntity<ErrorMessage> generateResponse(ResourceException e, Object... params) {
        return ResponseEntity.status(e.getHttpStatus()).body(getErrorMessage(e, params));
    }

    private ErrorMessage getErrorMessage(ResourceException e, Object... params) {
        return ErrorMessage.builder()
                           .code(e.getHttpStatus().value())
                           .codeDescription(e.getHttpStatus().getReasonPhrase())
                           .message(messageSource.getMessage(e.getMessageKey(), params))
                           .build();
    }
}