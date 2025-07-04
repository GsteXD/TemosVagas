<center>

***

# Documentação: Curriculo

</center>


#### **Pré-requisito: Autenticação**

Todas as operações a seguir exigem que o usuário esteja autenticado(Veja a documentação de autenticação para aprender a se autenticar) com o perfil de **CANDIDATO**. O sistema identifica o usuário logado através do contexto de segurança (`SecurityContextHolder`) para garantir que cada candidato manipule apenas o seu próprio currículo.

---

### **Passo 1: Criação Inicial do Currículo**

 O candidato cria a entidade principal `Curriculo`, que servirá como um contêiner para suas formações e o arquivo PDF. É possível, opcionalmente, já enviar as primeiras formações acadêmicas nesta mesma requisição.

*   **Endpoint**: `POST /candidato/curriculo/cadastrar`

**Lógica de Negócio:**
1.  O `CurriculoService` obtém o `Candidato` logado a partir do token de autenticação.
2.  **Validação**: O service verifica se este `Candidato` já possui um `Curriculo` associado no banco de dados. Se sim, uma exceção é lançada para impedir a duplicidade.
3.  Uma nova instância da entidade `Curriculo` é criada.
4.  O `Candidato` logado é associado a este novo `Curriculo`.
5.  Se a requisição (`CurriculoDTO`) tiver uma lista de formações, o service itera sobre ela:
    *   Cada `FormacaoDTO` é convertido para uma entidade `Formacao`.
    *   Cada entidade `Formacao` é associada ao `Curriculo` recém-criado.
6.  O `Curriculo` (e suas `Formacoes`, em cascata) é salvo no banco de dados.

**Exemplo de Requisição JSON (Completa):**
````json
{
  "formacao": [
    {
      "curso": "Análise e Desenvolvimento de Sistemas",
      "instituicao": "FATEC",
      "tipoDiploma": "Tecnólogo",
      "dataInicio": "08/2023",
      "dataFim": "12/2026"
    },
    {
      "curso": "Técnico em Informática",
      "instituicao": "ETEC",
      "tipoDiploma": "Técnico",
      "dataInicio": "01/2020",
      "dataFim": "12/2021"
    }
  ]
}
````

---

### **Passo 2: Adicionar/Gerenciar Formações (Opcional)**

Caso o candidato queira adicionar mais formações acadêmicas posteriormente.

*   **Endpoint**: `POST /candidato/formacao/cadastrar`

**Lógica de Negócio:**
1.  O `FormacaoService` obtém o `Candidato` logado.
2.  Busca o `Curriculo` pertencente a este `Candidato`. Se o currículo não for encontrado (o Passo 1 não foi executado), uma exceção é lançada.
3.  O `FormacaoDTO` da requisição é convertido para uma entidade `Formacao`.
4.  A nova entidade `Formacao` é associada ao `Curriculo` existente.
5.  A `Formacao` é salva no banco de dados.

**Exemplo de Requisição JSON:**
````json
{
  "curso": "Curso de Inglês",
  "instituicao": "Cultura Inglesa",
  "tipoDiploma": "Certificado",
  "dataInicio": "01/2023",
  "dataFim": "12/2023"
}
````

---

### **Passo 3: Upload do Arquivo PDF**

O candidato anexa seu currículo em formato PDF.

*   **Endpoint**: `POST /candidato/curriculo/upload`
*   **Tipo de Requisição**: `multipart/form-data`

**Lógica de Negócio:**
1.  O `CurriculoService` obtém o `Candidato` logado e busca seu `Curriculo`.
2.  O arquivo (`MultipartFile`) recebido é validado:
    *   Verifica se o arquivo não está vazio.
    *   Verifica se o tipo de conteúdo (MIME type) é `application/pdf`.
3.  Se a validação for bem-sucedida, o conteúdo do arquivo é lido como um array de bytes (`byte[]`).
4.  O array de bytes é salvo no campo `arquivoPdf` da entidade `Curriculo`.
5.  A entidade `Curriculo` é atualizada no banco de dados.

**Como a requisição é feita (Postman)**:
- Altere o método HTTP para `POST`.
- Insira a URL completa do endpoint: `http://localhost:8080/TemosVagas/api/candidato/curriculo/upload/`.

   **Selecionar form-data**:
   - Na aba Body, clique em "form-data"
   - Na coluna Key: `file`
   - Clique no dropdown ao lado e selecione "File"
   - Clique em "Select Files" e escolha seu arquivo PDF

   **Adicionar Authorization**:
   - Vá para a aba "Headers" e adcione o token jwt.

    **Envie a Requisição:**
    *   Com tudo configurado, clique no botão azul **Send**.

Se tudo estiver correto, você receberá uma resposta do servidor confirmando que o upload deu certo.

---

### **Passo 4: Consulta e Download**

#### **A. Listar Dados do Currículo**

Para visualizar os dados estruturados que foram cadastrados.

*   **Endpoint**: `GET /candidato/curriculo/listar`
*   **Lógica**: Busca o currículo do candidato logado e retorna um DTO contendo seus dados e a lista de formações.

**Exemplo de Resposta JSON:**
````json
{
  "id": 1,
  "candidatoId": 123,
  "formacao": [
    {
      "id": 1,
      "curso": "Análise e Desenvolvimento de Sistemas",
      "instituicao": "FATEC",
      "tipoDiploma": "Tecnólogo",
      "dataInicio": "02/2022",
      "dataFim": "12/2024"
    },
    {
      "id": 2,
      "curso": "Técnico em Informática",
      "instituicao": "ETEC",
      "tipoDiploma": "Técnico",
      "dataInicio": "01/2020",
      "dataFim": "12/2021"
    }
  ]
}
````

#### **B. Download do Arquivo PDF**

Para baixar o arquivo PDF previamente enviado.

*   **Endpoint**: `GET /candidato/curriculo/download`