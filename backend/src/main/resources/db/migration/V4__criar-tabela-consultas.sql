CREATE TABLE consultas (
    id          BIGSERIAL PRIMARY KEY,
    medico_id   BIGINT    NOT NULL REFERENCES medicos(id),
    paciente_id BIGINT    NOT NULL REFERENCES pacientes(id),
    data_hora   TIMESTAMP NOT NULL
);
