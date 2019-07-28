package br.com.pagamento.transaction.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="operation_type")
public class OperationType implements Serializable {

    private static final long serialVersionUID = 8614351452967790707L;

    public static final int COMPRA_A_VISTA = 1;
    public static final int COMPRA_PARCELADA = 2;
    public static final int SAQUE = 3;
    public static final int PAGAMENTO = 4;

    @Id
    @EqualsAndHashCode.Include
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="operation_type_id")
    private Long id;

    private String description;

    @Column(name="charge_order")
    private int chargeOrder;
}