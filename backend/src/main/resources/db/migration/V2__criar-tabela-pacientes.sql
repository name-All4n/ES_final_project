CREATE TABLE pacientes (
    id          BIGSERIAL    PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    telefone    VARCHAR(20)  NOT NULL,
    cpf         VARCHAR(11)  NOT NULL UNIQUE,
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,

    -- endereço embutido (Endereco @Embeddable)
    cidade      VARCHAR(100) NOT NULL,
    bairro      VARCHAR(100) NOT NULL,
    rua         VARCHAR(150) NOT NULL,
    cep         VARCHAR(8)   NOT NULL,
    uf          VARCHAR(2)   NOT NULL,
    complemento VARCHAR(100),
    numero      VARCHAR(10)
);
