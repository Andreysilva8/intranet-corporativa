CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    username VARCHAR(60) NOT NULL,
    email VARCHAR(120) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em DATETIME(6) NOT NULL,
    atualizado_em DATETIME(6) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_username UNIQUE (username),
    CONSTRAINT uk_usuarios_email UNIQUE (email),
    INDEX idx_usuarios_nome (nome),
    INDEX idx_usuarios_ativo (ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuario_roles (
    usuario_id BIGINT NOT NULL,
    role VARCHAR(30) NOT NULL,
    PRIMARY KEY (usuario_id, role),
    CONSTRAINT uk_usuario_roles_usuario_role UNIQUE (usuario_id, role),
    CONSTRAINT fk_usuario_roles_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE colaboradores (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome_completo VARCHAR(140) NOT NULL,
    email_corporativo VARCHAR(140) NOT NULL,
    telefone VARCHAR(30) NULL,
    ramal VARCHAR(20) NULL,
    departamento VARCHAR(120) NOT NULL,
    cargo VARCHAR(120) NOT NULL,
    localidade VARCHAR(120) NULL,
    foto_url VARCHAR(255) NULL,
    bio TEXT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    usuario_id BIGINT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_em DATETIME(6) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_colaboradores_email_corporativo UNIQUE (email_corporativo),
    CONSTRAINT uk_colaboradores_usuario UNIQUE (usuario_id),
    CONSTRAINT fk_colaboradores_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id)
        ON DELETE SET NULL,
    INDEX idx_colaboradores_nome (nome_completo),
    INDEX idx_colaboradores_departamento (departamento),
    INDEX idx_colaboradores_cargo (cargo),
    INDEX idx_colaboradores_ativo (ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE recados (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(160) NOT NULL,
    mensagem TEXT NOT NULL,
    data_publicacao DATETIME(6) NOT NULL,
    data_expiracao DATETIME(6) NULL,
    destaque BOOLEAN NOT NULL DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    autor_id BIGINT NOT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_em DATETIME(6) NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_recados_autor
        FOREIGN KEY (autor_id)
        REFERENCES usuarios (id),
    INDEX idx_recados_ativo (ativo),
    INDEX idx_recados_data_publicacao (data_publicacao),
    INDEX idx_recados_destaque (destaque)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE links_rapidos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(120) NOT NULL,
    url VARCHAR(500) NOT NULL,
    icone VARCHAR(80) NULL,
    categoria VARCHAR(80) NULL,
    ordem_exibicao INT NOT NULL DEFAULT 0,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em DATETIME(6) NOT NULL,
    atualizado_em DATETIME(6) NULL,
    PRIMARY KEY (id),
    INDEX idx_links_rapidos_ativo (ativo),
    INDEX idx_links_rapidos_categoria (categoria),
    INDEX idx_links_rapidos_ordem (ordem_exibicao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE documentos_corporativos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(160) NOT NULL,
    descricao TEXT NULL,
    categoria VARCHAR(80) NOT NULL,
    nome_arquivo_original VARCHAR(255) NOT NULL,
    nome_arquivo_armazenado VARCHAR(255) NOT NULL,
    content_type VARCHAR(120) NOT NULL,
    tamanho_bytes BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    enviado_por_id BIGINT NOT NULL,
    criado_em DATETIME(6) NOT NULL,
    atualizado_em DATETIME(6) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_documentos_nome_arquivo_armazenado UNIQUE (nome_arquivo_armazenado),
    CONSTRAINT fk_documentos_enviado_por
        FOREIGN KEY (enviado_por_id)
        REFERENCES usuarios (id),
    INDEX idx_documentos_titulo (titulo),
    INDEX idx_documentos_categoria (categoria),
    INDEX idx_documentos_ativo (ativo),
    INDEX idx_documentos_criado_em (criado_em)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE documento_roles_permitidas (
    documento_id BIGINT NOT NULL,
    role VARCHAR(30) NOT NULL,
    PRIMARY KEY (documento_id, role),
    CONSTRAINT uk_documento_roles_documento_role UNIQUE (documento_id, role),
    CONSTRAINT fk_documento_roles_documento
        FOREIGN KEY (documento_id)
        REFERENCES documentos_corporativos (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
