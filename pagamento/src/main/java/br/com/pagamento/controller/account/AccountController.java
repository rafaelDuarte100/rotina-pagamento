package br.com.pagamento.controller.account;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.pagamento.messages.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pagamento.dto.account.AccountDTO;
import br.com.pagamento.dto.account.AvailableLimitDTO;
import br.com.pagamento.model.account.Account;
import br.com.pagamento.service.account.AccountService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "Retorna todas as contas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista todas as contas cadastradas.", response = AccountDTO[].class),
            @ApiResponse(code = 500, message = "Ocorreu um erro não esperado.", response = ErrorMessage.class),
    })
    @GetMapping(value = "/limits", produces="application/json")
    public List<AccountDTO> findAll() {
        return accountService.findAll()
                             .stream()
                             .map(account -> convertToDTO(account))
                             .collect(Collectors.toList());
    }

    @ApiOperation(value = "Busca uma conta por ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a conta encontrada pelo ID informado.", response = AccountDTO.class),
            @ApiResponse(code = 404, message = "A operação não pôde ser concluída porque a conta de Id informado, não existe.", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro não esperado.", response = ErrorMessage.class),
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public AccountDTO findById(@PathVariable(name = "id", required = true) Long id) {
        return convertToDTO(accountService.findById(id));
    }

    @ApiOperation(value = "Cria uma nova conta.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a conta cadastrada.", response = AccountDTO.class),
            @ApiResponse(code = 406, message = "Não é possível criar uma nova conta com limite negativo.", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro não esperado.", response = ErrorMessage.class),
    })
    @PostMapping(produces = "application/json")
    public AccountDTO create(@RequestBody(required = true) AccountDTO accountDTO) {
        return convertToDTO(accountService.create(convertToAccount(accountDTO)));
    }

    @ApiOperation(value = "Atualiza a conta de ID informado, somando os limites informados aos pré-existentes na base.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a conta com os dados atualizados.", response = AccountDTO.class),
            @ApiResponse(code = 404, message = "A operação não pôde ser concluída porque a conta de Id informado, não existe.", response = ErrorMessage.class),
            @ApiResponse(code = 406, message = "A operação não pôde ser concluída porque resultaria em uma conta com limite negativo.", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro não esperado.", response = ErrorMessage.class),
    })
    @PatchMapping(value = "/{id}", produces = "application/json")
    public AccountDTO update(@PathVariable(name = "id", required = true) Long id, @RequestBody AccountDTO accountDTO) {
        Account account = convertToAccount(accountDTO);
        account.setId(id);
        return convertToDTO(accountService.updateLimits(account));
    }
    
    public AccountDTO convertToDTO(Account account) {
    	return AccountDTO.builder().id(account.getId())
		                 .availableCreditLimit(new AvailableLimitDTO(account.getAvailableCreditLimit()))
		                 .availableWithdrawalLimit(new AvailableLimitDTO(account.getAvailableWithdrawalLimit()))
		                 .build();
    }
    
    public Account convertToAccount(AccountDTO accountDTO) {
    	return Account.builder().id(accountDTO.getId())
								.availableCreditLimit(Optional.ofNullable(accountDTO.getAvailableCreditLimit())
															  .map(availableCreditLimit -> availableCreditLimit.getAmount())
															  .orElse(null))
								.availableWithdrawalLimit(Optional.ofNullable(accountDTO.getAvailableWithdrawalLimit())
															  .map(availableWithdrawalLimit -> availableWithdrawalLimit.getAmount())
															  .orElse(null))
				                .build();
    }
}