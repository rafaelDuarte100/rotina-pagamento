# Rotina de Pagamento

Projeto para simulação de rotinas de pagamentos de uma processadora de crédito.

## Operações Disponíveis

### Cadastro de Contas:

```
POST http://localhost:8080/rotina-pagamento/v1/accounts
```
```JSON
{
	"available_credit_limit": {
		"amount": 500.0
	},
	"available_withdrawal_limit": {
		"amount": 500.0
	}
}
```

Resposta:
```JSON
{
    "account_id": 1,
    "available_credit_limit": {
        "amount": 500.0
    },
    "available_withdrawal_limit": {
        "amount": 500.0
    }
}
```


### Atualização do Limite de Conta:

```
PATCH http://localhost:8080/rotina-pagamento/v1/accounts/{account_id}
```
```JSON
{
	"available_credit_limit": {
		"amount": 100.0
	},
	"available_withdrawal_limit": {
		"amount" : 100.0
	}
}
```

Resposta:
```JSON
{
    "account_id": 0,
    "available_credit_limit": {
        "amount": 100.0
    },
    "available_withdrawal_limit": {
        "amount": 100.0
    }
}
```

Obs: Enviar um dos limites com um valor negativo, irá subtrair do limite da conta.

### Consulta de Limites de Contas Cadastradas:

```
GET http://localhost:8080/rotina-pagamento/v1/accounts/limits
```
Resposta:
```JSON
[
    {
        "account_id": 0,
        "available_credit_limit": {
            "amount": 0
        },
        "available_withdrawal_limit": {
            "amount": 0
        }
    }
]    
```

### Consulta de Transações

```
GET http://localhost:8080/rotina-pagamento/v1/transactions
```
Resposta:

```JSON
[
    {
        "amount": 0.0,
        "balance": 0.0,
        "transaction_id": 0,
        "account_id": 0,
        "operation_type_id": 0,
        "event_date": null,
        "due_date": null
    }
]
```

### Consulta de Transações por ID

```
GET http://localhost:8080/rotina-pagamento/v1/transactions/{id}
```
Resposta:

```JSON
    {
        "amount": 0.0,
        "balance": 0.0,
        "transaction_id": 0,
        "account_id": 0,
        "operation_type_id": 0,
        "event_date": null,
        "due_date": null
    }
```

### Consulta de Transações por Conta

```
GET http://localhost:8080/rotina-pagamento/v1/transactions/account/{account_id}
```
Resposta:

```JSON
[
    {
        "amount": 0.0,
        "balance": 0.0,
        "transaction_id": 0,
        "account_id": 0,
        "operation_type_id": 0,
        "event_date": null,
        "due_date": null
    },
    {
        "amount": 0.0,
        "balance": 0.0,
        "transaction_id": 0,
        "account_id": 0,
        "operation_type_id": 0,
        "event_date": null,
        "due_date": null
    }
]
```

### Cadastro de Transações

```
POST http://localhost:8080/rotina-pagamento/v1/transactions
```
```JSON
{
	"account_id": 0,
	"operation_type_id": 0,
	"amount": 0
}
```

Resposta:

```JSON
{
    "transaction_id": 0,
    "account_id": 0,
    "operation_type_id": 0,
    "amount": 0,
    "balance": 0,
    "event_date": 0,
    "due_date": 0
}
```

Obs: Não é permitido informar um valor negativo para o campo 'amount'.

### Cadastro de Lista de Pagamentos

```
POST http://localhost:8080/rotina-pagamento/v1/payments
```
```JSON
[
	{
		"account_id": 0,
		"amount": 0
	},
	{
		"account_id": 0,
		"amount": 0
	}
]
```

Resposta:

```JSON
[
    {
        "amount": 0,
        "balance": 0,
        "transaction_id": 0,
        "account_id": 0,
        "operation_type_id": 0,
        "event_date": null,
        "due_date": null
    }
]
```

Obs: Não é permitido cadastrar um pagamento para uma conta que não possua contas a pagar, caso algum dos pagamentos informados na requisição quebre essa regra, ele será ignorado sem impedir que os demais sejam efetuados.

## Tipos de Operações

A tabela abaixo lista os tipos de operações possíveis para realização de transações.

| operation_type_id | descrição        |
|-------------------|------------------|
| 1                 | COMPRA À VISTA   |
| 2                 | COMPRA PARCELADA |
| 3                 | SAQUE            |
| 4                 | PAGAMENTO        |

## Dependências
* Java 8
* Maven 3
* Docker
* Docker-Compose
* Swagger

## Executando o projeto:
```SHELL
> git clone https://github.com/rafaelDuarte100/rotina-pagamento.git
> cd rotina-pagamento/pagamento/
> docker-compose up
```

## Executando os testes:
```SHELL
> git clone https://github.com/rafaelDuarte100/rotina-pagamento.git
> cd rotina-pagamento/pagamento/
> mvn test
```
## Swagger:
* Com a aplicação rodando acesse: http://localhost:8080/rotina-pagamento/v1/swagger-ui.html

## Endereço da aplicação no heroku:
* [Rotina pagamentos](http://rotina-pagamentos.herokuapp.com/rotina-pagamento/v1/swagger-ui.html)

## Autores
* **Rafael Duarte de Oliveira** - *Developer* - [rafaelduarte100@gmail.com](mailto:rafaelduarte100@gmail.com)
