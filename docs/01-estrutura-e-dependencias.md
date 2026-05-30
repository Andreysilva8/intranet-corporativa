# Passo 1 - Estrutura e dependencias

Este projeto segue uma arquitetura MVC tradicional com camadas claras para facilitar manutencao, testes e evolucao dos modulos da intranet.

## Estrutura de pastas

```text
intranet-corporativa/
  pom.xml
  README.md
  .editorconfig
  src/
    main/
      java/br/com/empresa/intranet/
        IntranetApplication.java
        config/
        controller/
        dto/
          request/
          response/
        entity/
          enums/
        exception/
        repository/
        security/
        service/
          impl/
      resources/
        application.properties
        messages.properties
        db/
          migration/
        static/
          css/
            modules/
          img/
          js/
        templates/
          auth/
          colaboradores/
          dashboard/
          documentos/
          error/
          fragments/
          layout/
    test/
      java/br/com/empresa/intranet/
        IntranetApplicationTests.java
  storage/
    documents/
```

## Dependencias principais

- `spring-boot-starter-web`: controllers MVC, Tomcat embarcado e recursos web.
- `spring-boot-starter-thymeleaf`: renderizacao server-side das paginas HTML.
- `thymeleaf-extras-springsecurity6`: exibicao condicional de elementos por autenticacao/roles nas views.
- `spring-boot-starter-security`: login, filtros de seguranca, CSRF e autorizacao.
- `spring-boot-starter-data-jpa`: persistencia com JPA/Hibernate.
- `spring-boot-starter-validation`: validacao de formularios, DTOs e entidades.
- `spring-boot-starter-actuator`: endpoints operacionais, como health check.
- `mysql-connector-j`: driver JDBC do MySQL em runtime.
- `spring-boot-devtools`: produtividade no desenvolvimento local.
- `spring-boot-configuration-processor`: metadados para propriedades tipadas.
- `spring-boot-starter-test`, `spring-security-test` e `h2`: base de testes.

## Observacoes

- O projeto usa Java 17 como baseline.
- O `application.properties` ja deixa a conexao MySQL parametrizada por variaveis de ambiente.
- O armazenamento inicial de documentos aponta para `storage/documents`, com limite de upload configurado em 10MB.
- O diretorio `db/migration` fica reservado para migracoes SQL quando definirmos o schema.
