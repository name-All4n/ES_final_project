ALTER TABLE usuarios
    ADD COLUMN medico_id BIGINT UNIQUE,
    ADD CONSTRAINT fk_usuarios_medico FOREIGN KEY (medico_id) REFERENCES medicos(id);