package br.com.pagamento.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.pagamento.model.account.Account;
import br.com.pagamento.util.account.AccountUtil;

@RunWith(Parameterized.class)
public class AccountUtilTests {

    private AccountUtil accountUtil = new AccountUtil();

    @Parameter(value=0)
    public Account account;

    @Parameter(value=1)
    public Double creditLimit;

    @Parameter(value=2)
    public Double WithdrawalLimit;

    @Parameter(value=3)
    public Double creditLimitExpected;

    @Parameter(value=4)
    public Double WithdrawalLimitExpected;

    @Parameter(value=5)
    public String scenario;

    public static Account accountScenario1 = Account.builder().build();
    public static Account accountScenario2 = Account.builder().availableCreditLimit(100D).availableWithdrawalLimit(100D).build();
    public static Account accountScenario3 = Account.builder().build();
    public static Account accountScenario4 = Account.builder().availableCreditLimit(100D).availableWithdrawalLimit(100D).build();
    public static Account accountScenario5 = Account.builder().availableCreditLimit(100D).availableWithdrawalLimit(100D).build();

    @Parameters(name="{5}")
    public static Collection<?> getParametros(){
		return Arrays.asList(new Object[][] {
            { accountScenario1, null, null, null, null, "Quando tentar adicionar credito nulo em conta com crédito nulo, então conta permanece com crédito nulo." }
            ,{ accountScenario2, null, null, 100D, 100D, "Quando tentar adicionar credito nulo em conta com crédito não nulo, então conta permanece com crédito original." }
            ,{ accountScenario3, 100D, 100D, 100D, 100D, "Quando tentar adicionar credito não nulo em conta com crédito nulo, então conta fica com crédito igual ao adicionado." }
            ,{ accountScenario4, 150.45, 150.45, 250.45, 250.45, "Quando tentar adicionar credito não nulo em conta com crédito não nulo, então conta fica com crédito igual a soma dos créditos." }
            ,{ accountScenario5, -77.77, -77.77, 22.23, 22.23, "Quando tentar adicionar credito negativo em conta com crédito não nulo, então conta fica com crédito igual a subtração dos créditos." }
        });
	}

    @Test
    public void updateAccountLimitTest() {
        accountUtil.updateAccountLimits(account, creditLimit, WithdrawalLimit);
        assertEquals(creditLimitExpected, account.getAvailableCreditLimit());
        assertEquals(WithdrawalLimitExpected, account.getAvailableWithdrawalLimit());
    }
    
}