<center>

***

## Documentação: Cadastro de Usuário
</center>

O sistema permite o cadastro de dois tipos de usuários: **Candidatos** e **Empresas**. Cada tipo possui um endpoint e um fluxo de cadastro específico.

### 1. Cadastro de Candidato

O cadastro de um novo candidato é realizado através de uma requisição `POST` para o endpoint `/candidato/cadastrar`.

-   **Endpoint:** `POST /candidato/cadastrar`

#### Fluxo do Processo:

1.  A requisição chega ao método `cadastrar` no `CandidatoController`.
2.  Os dados do corpo da requisição, representados pelo `CandidatoDTO`, são validados.
3.  O DTO é convertido para a entidade `Candidato`.
4.  O método `cadastrar` do `CandidatoService` é chamado.
5.  No service, a senha do candidato é criptografada usando `BCryptPasswordEncoder`.
6.  O `EmailService` é acionado para enviar um e-mail de boas-vindas para o novo candidato.
7.  O `Candidato` é salva no banco de dados com a role `ROLE_CANDIDATO`.
8.  A resposta retorna um `ResponseEntity` com status `200 OK` e o `CandidatoDTO` dos dados salvos.

#### Corpo da Requisição (Request Body)

````json
{
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "telefone": "(11)91111-1111",
    "email": "joao.silva@gmail.com",
    "senha": "senha",
    "endereco": {
        "pais": "Brasil",
        "cidade": "São Paulo",
        "estado": "SP",
        "cep": "01111-111"
    }
}
````
####  Resposta (Reponse Body)

````json
{
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "telefone": "(11)91111-1111",
    "email": "joao.silva@gmail.com",
    "senha": "$2a$10$fyDC5OvEtgogULLaljZaceLP1VhPfS6InqqLzHCDQEhWerI1Adbhu",
    "endereco": {
        "pais": "Brasil",
        "cidade": "São Paulo",
        "estado": "SP",
        "cep": "01111-111"
    }
}
````


### 2. Cadastro de Empresa

O cadastro de uma nova empresa é realizado através de uma requisição `POST` para o endpoint `/empresa/cadastrar`.

-   **Endpoint:** `POST /empresa/cadastrar`

#### Fluxo do Processo:

1.  A requisição chega ao método `cadastrar` no `EmpresaController`.
2.  Os dados do corpo da requisição, representados pelo `EmpresaDTO`, são validados.
3.  O DTO é convertido para a entidade `Empresa`.
4.  O método `cadastrar` do `EmpresaService` é chamado.
5.  No service, a senha da empresa é criptografada usando `BCryptPasswordEncoder`.
6.  A `Empresa` é salva no banco de dados com a papel `ROLE_EMPRESA`.
7.  A resposta retorna um `ResponseEntity` com status `200 OK` e o `EmpresaDTO` dos dados salvos.

#### Corpo da Requisição (Request Body)

````json
{
    "nome": "Empresa T.I",
    "cnpj": "12.345.678/0001-99",
    "grupo": "Tecnologia",
    "telefone": "(11)1234-5678",
    "email": "contato@gmail.com",
    "senha": "senha",
    "endereco": {
        "pais": "Brasil",
        "cidade": "Rio de Janeiro",
        "estado": "RJ",
        "cep": "20040-000"
    }
}
````
####  Resposta (Reponse Body)

````json
{
    "nome": "Empresa T.I",
    "cnpj": "12.345.678/0001-99",
    "grupo": "Tecnologia",
    "telefone": "(11)91234-5678",
    "email": "contato@gmail.com",
    "senha": "$2a$10$KzEuxltHJcbiXuwDNN3dgOKuqQuzfo9ezxbH3KOQuvC.NpF52hgPK",
    "endereco": {
        "pais": "Brasil",
        "cidade": "Rio de Janeiro",
        "estado": "RJ",
        "cep": "20040-000"
    }
}
---