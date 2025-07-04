<center>

***

# Documentação: Aplicação para Vagas
</center>

### 1. Visão Geral

O objetivo desta funcionalidade é gerenciar o ciclo de vida das candidaturas a vagas. Ela permite que um candidato se inscreva em uma vaga, consulte suas candidaturas, e que a empresa gerencie os candidatos inscritos em suas vagas. O sistema executa uma série de validações de negócio para garantir a integridade do processo.

### 2. Pré-condições

*   O usuário deve estar autenticado na plataforma (veja a documentação de Autenticação).
*   Para se candidatar ou ver suas candidaturas, o usuário deve ter um perfil de `CANDIDATO`.
*   Para gerenciar candidaturas de uma vaga, o usuário deve ter um perfil de `EMPRESA`.
*   O candidato deve possuir um currículo cadastrado e, dependendo da vaga, uma formação acadêmica associada.

### 3. Endpoints da API

Abaixo estão listadas todas as operações disponíveis no recurso de aplicação.

---

#### 3.1. Candidatar-se a uma Vaga

Permite que um candidato autenticado se inscreva em uma vaga.

*   **Endpoint**: `POST /TemosVagas/api/aplicacao/candidatar`
*   **Quem usa:**: `CANDIDATO`
*   **Autenticação**: Requer token JWT.
*   **Corpo da Requisição (`AplicacaoDTO`)**:
    ```json
    {
      "vagaId": 1,
      "informacaoAdicionais": "5 anos de experiência em desenvolvimento Back-End"
    }
    ```
*   **Resposta (Sucesso 200 OK)**:
    ```json
    {
      "id": 12,
      "nomeCandidato": "João Silva",
      "tituloVaga": "Desenvolvedor Back-End Pleno",
      "nomeEmpresa": "Empresa T.I",
      "dataAplicacao": "04/07/2025 14:20:10",
      "status": "PENDENTE",
      "informacoesAdicionais": "5 anos de experiência em desenvolvimento Back-End"
    }
    ```
---

#### 3.2. Listar Minhas Candidaturas

Retorna a lista de todas as vagas às quais o candidato autenticado se aplicou.

*   **Endpoint**: `GET /TemosVagas/api/aplicacao/minhasCandidaturas`
*   **Quem usa:**: `CANDIDATO`
*   **Autenticação**: Requer token JWT.
*   **Resposta (Sucesso 200 OK)**: Uma lista de aplicações.
    ```json
    [
      {
        "id": 12,
        "nomeCandidato": "João Silva",
        "tituloVaga": "Desenvolvedor Back-End Pleno",
        "nomeEmpresa": "Empresa T.I",
        "dataAplicacao": "04/07/2025 14:20:10",
        "status": "PENDENTE",
        "informacoesAdicionais": "5 anos de experiência em desenvolvimento Back-End"
      },
      {
        "id": 15,
        "nomeCandidato": "João Silva",
        "tituloVaga": "Desenvolvedor Back-End Pleno",
        "nomeEmpresa": "Software House",
        "dataAplicacao": "02/07/2025 10:15:00",
        "status": "EM_ANALISE",
        "informacoesAdicionais": "Experiência em Java."
      }
    ]
    ```

---

#### 3.3. Listar Candidaturas por Vaga

Retorna a lista de todas as candidaturas para uma vaga específica.

*   **Endpoint**: `GET /TemosVagas/api/aplicacao/vaga/{vagaId}`
*   **Quem usa:**: `EMPRESA`
*   **Autenticação**: Requer token JWT.
*   **Parâmetros de URL**:
    *   `vagaId` (Long): O ID da vaga a ser consultada.
*   **Exemplo de URL**: `/TemosVagas/api/aplicacao/vaga/1`
*   **Resposta (Sucesso 200 OK)**: Uma lista de aplicações para a vaga especificada.

 ```json
    [
      {
        "id": 12,
        "nomeCandidato": "João Silva",
        "tituloVaga": "Desenvolvedor Back-End Pleno",
        "nomeEmpresa": "Empresa T.I",
        "dataAplicacao": "04/07/2025 14:20:10",
        "status": "PENDENTE",
        "informacoesAdicionais": "5 anos de experiência em desenvolvimento Back-End"
      },
      {
        "id": 15,
        "nomeCandidato": "Lucas Santos",
        "tituloVaga": "Desenvolvedor Back-End Pleno",
        "nomeEmpresa": "Empresa T.I",
        "dataAplicacao": "02/07/2025 10:15:00",
        "status": "EM_ANALISE",
        "informacoesAdicionais": "Experiência em Java."
      }
    ]
````

#### 3.4. Atualizar Status da Candidatura

Permite que a empresa altere o status de uma candidatura.

*   **Endpoint**: `PUT /TemosVagas/api/aplicacao/status/{aplicacaoId}/{status}`
*   **Quem usa:**: `EMPRESA`
*   **Autenticação**: Requer token JWT.
*   **Parâmetros de URL**:
    *   `aplicacaoId` (Long): O ID da aplicação a ser atualizada.
    *   `status` (String): O novo status da aplicação. Valores possíveis: `PENDENTE`, `EM_ANALISE`, `APROVADO`, `REJEITADO`.
*   **Exemplo de URL**: `/TemosVagas/api/aplicacao/status/12/APROVADO`
*   **Resposta (Sucesso 200 OK)**: A aplicação com o status atualizado.
    ```json
    {
      "id": 12,
      "nomeCandidato": "João Silva",
      "tituloVaga": "Desenvolvedor Back-End Pleno",
      "nomeEmpresa": "Empresa T.I",
      "dataAplicacao": "04/07/2025 14:20:10",
      "status": "APROVADO",
      "informacoesAdicionais": "5 anos de experiência em desenvolvimento Back-End"
    }
    ```

### 4. Fluxo: Candidatar-se a uma Vaga

Esta seção detalha o passo a passo do processo iniciado pela requisição `POST /aplicacao/candidatar`.

#### Passo 1: Requisição do Cliente

O candidato dispara uma requisição `POST` para o servidor para o endpoint `/candidatar`. A requisição deve conter um token JWT válido no cabeçalho `Authorization` e um corpo JSON (`AplicacaoDTO`) com o ID da vaga.

#### Passo 2: Camada de Controller (`AplicacaoController`)

O `AplicacaoController` recebe a requisição. O método `candidatarVaga` é acionado, deserializa o JSON para um objeto `AplicacaoDTO` e chama o service.

#### Passo 3: Camada Service (`AplicacaoService`) - Lógica Principal

Esta camada define toda a lógica de negócio.

1.  **Obter Usuário Logado**: Busca os dados do candidato autenticado.
2.  **Buscar Vaga**: Recupera a vaga pelo ID.
3.  **Validação 1: Status da Vaga**: Verifica se a vaga não está `FECHADA`.
4.  **Validação 2: Aplicação Duplicada**: Verifica se o candidato já se aplicou a esta vaga.
5.  **Validação 3: Elegibilidade do Candidato**: Chama o método `validarElegibilidadeCandidato` para as validações específicas.

#### Passo 4: Validação de Elegibilidade (`validarElegibilidadeCandidato`)

Este método usa um `switch` no `tipoVaga` para determinar se o candidato é elegível.

*   **Caso `ESTAGIO`**: Valida currículo, formação acadêmica(precisa estar cursando uma graduação), curso compatível e semestre mínimo.
*   **Caso `TRAINEE`**: Valida currículo, formação, se já é formado e se o ano de conclusão está no intervalo aceito.
*   **Caso `Default` (CLT, PJ, etc.)**: Valida apenas se o candidato possui um currículo cadastrado.

#### Passo 5: Persistência (`AplicacaoRepository`)

Se todas as validações forem aprovadas, uma nova entidade `Aplicacao` é criada e salva no banco de dados. Um gatilho `prePersist` define automaticamente a `dataAplicacao` e o `status` inicial como `PENDENTE`.

#### Passo 6: Resposta ao Cliente

O controller recebe a entidade `Aplicacao` salva, a converte para um `AplicacaoReponseDTO` e a envia como resposta `HTTP 200 OK` ao cliente.

### 5. Tratamento de Erros

O sistema captura exceções lançadas pela camada de serviço e as retorna em um formato padronizado.

**Exemplo de JSON de Resposta (Erro de Validação):**
```json
{
    "erro": "Erro interno: Você já se candidatou para esta vaga.",
    "codStatus": 500
}
```
**Exemplo de JSON de Resposta (Erro de Elegibilidade):**
```json
{
    "erro": "Erro interno: Erro ao validar semestre: É necessario estar no 5º Semestre.",
    "codStatus": 500
}
```