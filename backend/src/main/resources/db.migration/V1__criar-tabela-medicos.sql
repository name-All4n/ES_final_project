CREATE TABLE doctors (
    id           BIGSERIAL    PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    telefone     VARCHAR(20)  NOT NULL,
    crm          VARCHAR(6)   NOT NULL UNIQUE,
    especialidade VARCHAR(50) NOT NULL,
    ativo        BOOLEAN      NOT NULL DEFAULT TRUE,

-- endereço embutido (Endereco @Embeddable)
    cidade       VARCHAR(100) NOT NULL,
    bairro       VARCHAR(100) NOT NULL,
    rua          VARCHAR(150) NOT NULL,
    cep          VARCHAR(8)   NOT NULL,
    uf           CHAR(2)      NOT NULL,
    complemento  VARCHAR(100),
    numero       VARCHAR(10)
);