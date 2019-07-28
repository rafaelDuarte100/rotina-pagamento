package br.com.pagamento.account.model;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AccountTeste {

    @Parameter
    public Double availableLimitAdd;

    @Parameter(value=1)
    public Double availableLimitCurrent;

    @Parameter(value=2)
    public Double availableLimitExpected;

    @Parameter(value=3)
	public String cenario;

    @Parameters(name="{3}")
    public static Collection<?> getParametros(){
		return Arrays.asList(new Object[][] {
            { null, null, null, "Quando tentar adicionar credito nulo em conta com crédito nulo, então conta permanece com crédito nulo." }
            ,{ null, 100.0, 100.0, "Quando tentar adicionar credito nulo em conta com crédito não nulo, então conta permanece com crédito original." }
            ,{ 100.0, null, 100.0, "Quando tentar adicionar credito não nulo em conta com crédito nulo, então conta fica com crédito igual ao adicionado." }
            ,{ 100.0, 150.45, 250.45, "Quando tentar adicionar credito não nulo em conta com crédito não nulo, então conta fica com crédito igual a soma dos créditos." }
            ,{ -77.77, 155.54, 77.77, "Quando tentar adicionar credito negativo em conta com crédito não nulo, então conta fica com crédito igual a subtração dos créditos." }
        });
	}

    @Test
    public void deveCalcularSomaDoLimiteDeCreditoDisponivel() {
        Account account = Account.builder()
                                 .availableCreditLimit(availableLimitCurrent)
                                 .build();
        account.addAvailableCreditLimit(availableLimitAdd);
        assertEquals(availableLimitExpected, account.getAvailableCreditLimit());
    }

    @Test
    public void deveCalcularSomaDoLimiteDeRetiradaDisponivel() {
        Account account = Account.builder()
                                 .availableWithdrawalLimit(availableLimitCurrent)
                                 .build();
        account.addAvailableWithdrawalLimit(availableLimitAdd);
        assertEquals(availableLimitExpected, account.getAvailableWithdrawalLimit());
    }
}