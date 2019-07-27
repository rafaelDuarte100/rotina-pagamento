package br.com.pagamento.account.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvailableLimitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double amount;    
}