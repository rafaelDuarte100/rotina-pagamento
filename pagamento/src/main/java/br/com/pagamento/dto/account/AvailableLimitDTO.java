package br.com.pagamento.dto.account;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvailableLimitDTO implements Serializable {
	
	private static final long serialVersionUID = -2539092894584891784L;
	private Double amount;    
}