package br.com.pagamento.account.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="account", schema="public")
public class Account implements Serializable {
	
	private static final long serialVersionUID = -2891410009810697423L;

	@Id
    @EqualsAndHashCode.Include
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="account_id")
	private Long id;
	
	@Column(name="available_credit_limit")
	@JsonSerialize(using = CustomAvailableLimitSerializer.class)
	@JsonDeserialize(using = CustomAvailableLimitDeserialize.class)
	@JsonProperty(value = "available_credit_limit")
	private Double availableCreditLimit;
	
	@Column(name="available_withdrawal_limit")
	@JsonProperty(value = "available_withdrawal_limit")
	@JsonSerialize(using = CustomAvailableLimitSerializer.class)
	@JsonDeserialize(using = CustomAvailableLimitDeserialize.class)
	private Double availableWithdrawalLimit;
	
}