package br.com.pagamento.account.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableLimit implements Serializable {

    private static final long serialVersionUID = -2674065794788048318L;
    
    double amount;
}