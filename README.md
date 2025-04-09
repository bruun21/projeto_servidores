## Dados Inscrições

 Bruno César Ramos Fraga

Número da inscrição: 9965
CPF: 35598539829
Perfil: DESENVOLVEDOR JAVA (BACK-END) - PLENO

Número da inscrição: 9978
CPF: 35598539829
Perfil: DESENVOLVEDOR JAVA (BACK-END) - SÊNIOR

Número da inscrição: 9989
CPF: 35598539829
Perfil: DESENVOLVEDOR FULL STACK - SÊNIOR

# Projeto Servidores

## Descrição
Este é um projeto demonstrativo desenvolvido com Spring Boot, projetado para gerenciamento de servidores com autenticação JWT, armazenamento de dados PostgreSQL e armazenamento de objetos MinIO.

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3.4.4
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (JSON Web Token)
- MinIO (Armazenamento de objetos)
- Docker e Docker Compose
- Lombok
- SpringDoc OpenAPI

## Pré-requisitos
- Java 21
- Maven
- Docker e Docker Compose
- PostgreSQL (para desenvolvimento local)

## Configuração e Execução

### Usando Docker Compose (Recomendado)
1. Clone o repositório
   ```
   git clone [URL_DO_REPOSITÓRIO]
   cd projeto
   ```

2. Configure o arquivo `.env` (um arquivo de exemplo já está incluído)

3. Certifique-se de que o serviço local do PostgreSQL esteja parado.

4. Execute com Docker Compose
   ```
   docker-compose up --build
   ```

5. A aplicação estará disponível em: `http://localhost:8080/swagger-ui.html`
   - Interface do MinIO: `http://localhost:9001`
   - Banco de dados PostgreSQL: `localhost:5432`

### Execução Local
1. Certifique-se de ter PostgreSQL instalado e configurado localmente

2. Configure as propriedades no arquivo `application.properties`

3. Execute o projeto com Maven
   ```
   mvn spring-boot:run
   ```

## Configurações

### Banco de Dados
- PostgreSQL configurado tanto para ambiente Docker quanto para desenvolvimento local
- Criação automática de tabelas (spring.jpa.hibernate.ddl-auto=create)

### Segurança
- Autenticação baseada em JWT
- Token de acesso: 5 minutos (em Docker), 50 minutos (local)
- Token de atualização: 30 dias
- Janela de atualização: 24 horas
- Usuário administrador criado automaticamente na inicialização

### Armazenamento de Objetos
- MinIO configurado para armazenamento de objetos
- Bucket padrão: mybucket

## Documentação da API
- A documentação OpenAPI está disponível em: `http://localhost:8080/swagger-ui.html`

## Usuário Administrador Padrão (Definido nas variáveis de ambiente)
- Email: admin@email.com
- Senha: Admin123
- Função: ADMIN


## Configuração Avançada
Consulte os arquivos `.env` e `application.properties` para personalizar as configurações de:
- Banco de dados
- JWT
- MinIO
- CORS
- Logs
