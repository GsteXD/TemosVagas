<center>

***

# Documentação: Autenticação
</center>

## Visão Geral

O sistema utiliza `Spring Security` para proteger os endpoints da API. A autenticação é realizada através de um endpoint de login que em caso de sucesso, retorna um `JSON Web Token`. Este token deve ser enviado no cabeçalho `Authorization` de todas as requisições subsequentes a endpoints protegidos. A autorização é baseada em papéis (Roles), definindo quais usuários (`CANDIDATO` ou `EMPRESA`) podem acessar determinados recursos.

---

## Fluxo de Autenticação

1.  **Requisição de Login**:
    *   O usuário (candidato ou empresa) envia uma requisição `POST` para `/autenticar/login`.
    *   O corpo da requisição contém as credenciais no formato JSON, conforme o DTO `LoginRequest`:
        ```json
        {
          "email": "usuario@gmail.com",
          "senha": "senha"
        }
        ```
    *   O endpoint é gerenciado pelo `AutenticacaoController`.

2.  **Processamento da Autenticação**:
    *   O controller chama o `AuthenticationService`, que utiliza o `AuthenticationManager` do Spring Security para validar as credenciais.
    *   O `AuthenticationManager` utiliza o `CustomUserDetailsService` para buscar o usuário pelo e-mail no banco de dados, verificando tanto a tabela de `Candidato` quanto a de `Empresa`.
    *   A senha fornecida é comparada com a senha criptografada (usando `BCrypt`) armazenada no banco.

3.  **Geração do JWT**:
    *   Se as credenciais forem válidas, o `JwtService` é chamado para gerar um token.
    *   O token contém o e-mail do usuário como "subject" e tem um tempo de expiração de 2 horas
    *   A chave secreta para assinar o token é lida do arquivo `application.properties`.

4.  **Resposta do Login**:
    *   O servidor responde com um status `200 OK` e um JSON contendo o token, o nome do usuário e seu papel (role).
        ```json
        {
          "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZW1vcy12YWdhcy1hcGkiLCJzdWIiOiJvbGl2ZWlyYXZpbmljaXVzMDEwQGdtYWlsLmNvbSIsImlhdCI6MTc1MTE2MDE2NywiZXhwIjoxNzUxMTY3MzY3fQ.7IHQFlg4T0MBhezIL8ivvANCd9ULacWDOWHiQctD6NQ",
          "nome": "Nome",
          "role": "ROLE_CANDIDATO"
        }
        ```

---

## Fluxo de Autorização (Requisições Autenticadas)

1.  **Envio do Token**:
    *   Para acessar endpoints protegidos, o cliente deve incluir o JWT recebido no cabeçalho `Authorization` da requisição, utilizando o prefixo `Bearer`.
        No Postman, selecione "Headers" em key coloque "Authorization" e na coluna value insira: Bearer 'Token'
    *   Exemplo: `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZW1vcy12YWdhcy1hcGkiLCJzdWIiOiJvbGl2ZWlyYXZpbmljaXVzMDEwQGdtYWlsLmNvbSIsImlhdCI6MTc1MTE2MDE2NywiZXhwIjoxNzUxMTY3MzY3fQ.7IHQFlg4T0MBhezIL8ivvANCd9ULacWDOWHiQctD6NQ`

2.  **Filtragem e Validação do Token**:
    *   O filtro `UserAuthenticationFilter` intercepta todas as requisições.
    *   Ele extrai o token do cabeçalho e utiliza o `JwtService` para validar sua assinatura e expiração.
    *   Se o token for válido, o e-mail do usuário é extraído.

3.  **Configuração do Contexto de Segurança**:
    *   Com o e-mail, o `CustomUserDetailsService` carrega os detalhes do usuário (`UserDetails`).
    *   O filtro então cria um objeto de autenticação e o armazena no `SecurityContextHolder`, tornando o usuário autenticado para a duração da requisição.

4.  **Controle de Acesso por Papel (Role)**:
    *   A classe `SecurityConfig` define as regras de autorização.
    *   Ela especifica quais papéis (Candidato ou Empresa) são necessários para acessar cada endpoint. Por exemplo:
        *   Endpoints em `/candidato/**` exigem o papel `ROLE_CANDIDATO`.
        *   Endpoints como `/empresa/**` e `/vaga/cadastrar` exigem o papel `ROLE_EMPRESA`.
    *   Se um usuário autenticado tentar acessar um recurso para o qual não tem permissão, o servidor retornará um erro `403 Forbidden`.

## Componentes Principais

*   **`SecurityConfig`**: Classe central de configuração do Spring Security. Define as regras de autorização, configura o `AuthenticationManager` e o `PasswordEncoder`.
*   **`JwtService`**: Responsável por gerar, validar e extrair informações de tokens JWT.
*   **`UserAuthenticationFilter`**: Filtro que intercepta requisições para validar o JWT e configurar o contexto de segurança.
*   **`AuthenticationService`**: Orquestra o processo de autenticação no login.
*   **`CustomUserDetailsService`**: Carrega os dados do usuário (Candidato ou Empresa) a partir do banco de dados.
*   **`Usuario`**: Superclasse que implementa `UserDetails`, fornecendo ao Spring Security as informações necessárias sobre o usuário (e-mail, senha, papéis).