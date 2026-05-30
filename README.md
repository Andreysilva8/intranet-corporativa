# Intranet Corporativa

Base MVC para uma intranet corporativa com Spring Boot, Spring Security, Spring Data JPA, Thymeleaf e MySQL.

## Stack

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL

## Status

Projeto MVC completo para a primeira versao da intranet:

- Login com Spring Security, CSRF e BCrypt.
- Controle de acesso por roles `ADMIN`, `RH` e `COLABORADOR`.
- Dashboard com mural de recados e links rapidos.
- Diretorio de colaboradores com busca e perfil.
- Upload e download de documentos corporativos.
- Camadas separadas em controllers, services, repositories, entities e security.
- Schema versionado com Flyway.

## Estrutura inicial

```text
intranet-corporativa/
  pom.xml
  docs/
    01-estrutura-e-dependencias.md
  src/
    main/
      java/br/com/empresa/intranet/
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
        IntranetApplication.java
      resources/
        db/migration/
        static/
          css/modules/
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
        application.properties
        messages.properties
    test/
      java/br/com/empresa/intranet/
        IntranetApplicationTests.java
  storage/
    documents/
```

## Dependencias do Maven

- Spring Boot Web
- Thymeleaf
- Thymeleaf Extras Spring Security
- Spring Security
- Spring Data JPA
- Bean Validation
- Actuator
- MySQL Driver
- DevTools
- Configuration Processor
- Spring Boot Test
- Spring MVC Test
- Spring Security Test
- H2 para testes

## Primeiro acesso

No primeiro start, a aplicacao cria um usuario administrador caso ele ainda nao exista:

- Usuario: `admin`
- Senha: `admin123`

Para sobrescrever em ambiente real:

```bash
ADMIN_USERNAME=admin
ADMIN_EMAIL=admin@empresa.local
ADMIN_PASSWORD=sua-senha-forte
```

## Execucao

Configure o MySQL e crie o banco:

```sql
CREATE DATABASE intranet_corporativa CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Depois execute:

```bash
mvn spring-boot:run
```

Acesse:

```text
http://localhost:8080
```
