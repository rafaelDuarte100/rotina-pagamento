package br.com.pagamento.messages;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = 7607673373050849771L;

    private Integer code;

    @JsonProperty("code_description")
    private String codeDescription;

    private String message;
}