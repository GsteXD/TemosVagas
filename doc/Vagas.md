<center>

***

# Documentação: Vagas 
</center>

### 1. Visão Geral

O sistema permite que entidades do tipo `Empresa` cadastrem, atualizem e gerenciem vagas de emprego. Candidatos (`Candidato`) podem visualizar as vagas abertas e se candidatar a elas. O ciclo de vida de uma vaga é controlado por status, que podem ser alterados tanto manualmente pela empresa quanto automaticamente pelo sistema.

### 2. Entidade Principal: `Vaga`

Seus principais atributos são:

*   `id`: Identificador único.
*   `empresa`: A `Empresa` que publicou a vaga.
*   `status`: O estado atual da vaga (ex: ABERTO, FECHADO).
*   `tipo`: Tipo da vaga (ex: "Estágio", "Trainee", "CLT").
*   `titulo`: Título da vaga.
*   `descricao`: Descrição detalhada.
*   `dataPub`: Data de publicação.
*   `dataLimite`: Data limite para candidaturas.
*   `semestre`: Semestre mínimo exigido (para estágio).
*   `curso`: Curso exigido (para estágio).
*   `cursoConclusao`: Ano de conclusão exigido (para trainee).

### 3. Ciclo de Vida de uma Vaga

Uma vaga pode passar por diferentes status, gerenciados principalmente no `VagaService`.

#### a. Cadastro (`cadastrar`)

*   Uma `Empresa` logada cria uma nova vaga através do endpoint `POST /vaga/cadastrar`.
*   O service `VagaService` associa a vaga à empresa logada, define a `dataPub` como a data atual e atribui o status inicial como **ABERTO**.
*   Antes de salvar, o método `validarVaga` é chamado para garantir a consistência dos dados com base no `tipo` da vaga.

##### Exemplos de JSON para Cadastro

**Vaga de Estágio:**
```json
{
    "tipo": "ESTAGIO",
    "titulo": "Estágio em Desenvolvimento Backend",
    "descricao": "O estagiário auxiliará no desenvolvimento e manutenção de APIs REST utilizando Java e Spring Boot.",
    "dataLimite": "2025-08-15",
    "semestre": 4,
    "curso": "Análise e Desenvolvimento de Sistemas"
}
```

**Vaga de Trainee:**
```json
{
    "tipo": "TRAINEE",
    "titulo": "Programa de Trainee - Tecnologia",
    "descricao": "Programa para recém-formados em cursos de tecnologia.",
    "dataLimite": "2025-07-31",
    "cursoConclusao": 2024
}
```

**Vaga CLT:**
```json
{
    "tipo": "CLT",
    "titulo": "Desenvolvedor Back-End Pleno",
    "descricao": "Responsável pelo desenvolvimento e manutenção de APIs.",
    "dataLimite": "2025-09-01"
}
```

#### b. Atualização (`atualizar`)

*   A empresa pode modificar uma vaga existente usando: `PUT /vaga/atualizar/{id}`, alterando o {id} para o id da vaga. Exemplo:`PUT /vaga/atualizar/1`
*   O service verifica se a `dataLimite` foi estendida. Se sim, o status da vaga é alterado para **PRORROGADO**.

#### c. Controle de Status

O status de uma vaga pode ser alterado de três formas:

1.  **Manualmente pela Empresa**: Através do endpoint `PUT /vaga/status/{id}`, a empresa pode definir o status como `ABERTO` ou `FECHADO`.
2.  **Fechamento Automático por Data**: Um método agendado em `VagaService` é executado diariamente para alterar o status para **FECHADO** de todas as vagas cuja `dataLimite` já passou.
3.  **Fechamento por Aprovação**: Quando uma `Aplicacao` a uma vaga tem seu status alterado para `APROVADO` no `AplicacaoService`, a vaga correspondente é automaticamente movida para o status **FECHADO**.

### 4. Validações de Negócio

O método `validarVaga` em `VagaService` aplica regras específicas com base no tipo da vaga:

*   **Estágio**: É obrigatório informar o `semestre` e o `curso`. O campo `cursoConclusao` não é permitido.
*   **Trainee**: É obrigatório informar o `cursoConclusao`.
*   **Outros Tipos**: A `descricao` é obrigatória.

Além disso, a `dataLimite` não pode ser anterior à data atual.

### 5. Endpoints da API (Controller)

As operações são expostas pelo `VagaController`:

| Método                                       | Rota                   | Descrição                                  | Autorização |
| :-------------------------------------------- | :--------------------- | :----------------------------------------- | :---------- |
| <span style="color:yellow;">**POST**</span>    | `/vaga/cadastrar`      | Cria uma nova vaga.                        | `EMPRESA`   |
| <span style="color:blue;">**PUT**</span>       | `/vaga/atualizar/{id}` | Atualiza os dados de uma vaga existente.   | `EMPRESA`   |
| <span style="color:blue;">**PUT**</span>       | `/vaga/status/{id}`    | Altera manualmente o status de uma vaga.    | `EMPRESA`   |
| <span style="color:green;">**GET**</span>      | `/vaga/find/{id}`      | Busca uma vaga específica pelo seu ID.     | `Candidato` ou `Empresa`Autenticado |
| <span style="color:green;">**GET**</span>      | `/vaga/listar`         | Lista todas as vagas com status `ABERTO`.  | `CANDIDATO` |

### 6. Interação com Candidaturas (`Aplicacao`)

*   **Listagem**: O endpoint `GET /vaga/listar` utiliza o método `listarVagasAbertas` de `VagaService` para retornar apenas vagas com status `ABERTO` para os candidatos.
*   **Aplicação**: O método `aplicarVaga` verifica se o status da vaga não é `FECHADO` antes de permitir uma nova candidatura.
*   **Validação de Elegibilidade**: Ao se candidatar, o `AplicacaoService` executa uma série de validações (`validarElegibilidadeCandidato`) para garantir que o perfil do candidato (curso, semestre, ano de conclusão) é compatível com os requisitos da vaga.

### 7. Segurança

O acesso aos endpoints de vagas é protegido pela classe `SecurityConfig`:

*   Operações de escrita (`POST`, `PUT`) em `/vaga/` são restritas a usuários com o papel `EMPRESA`.
*   A listagem de vagas (`GET /vaga/listar`) é restrita a usuários com o papel `CANDIDATO`.