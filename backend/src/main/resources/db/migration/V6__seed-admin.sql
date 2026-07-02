-- Usuário administrador inicial (bootstrap).
-- Sem este registro é impossível fazer o primeiro login: /auth/login exige um
-- usuário existente e POST /usuarios exige ROLE_ADMINISTRADOR (galinha-e-ovo).
--
-- Credenciais: login = admin@clinica.com  |  senha = admin123
-- A senha abaixo é o hash BCrypt (custo 10) de "admin123".
-- TROQUE a senha assim que entrar no sistema.
INSERT INTO usuarios (login, senha, papel)
VALUES (
    'admin@clinica.com',
    '$2b$10$dCMssUaKp.tQkYjauncNoO6XQ7nXv1yX/kCAa4QZkNUmAQDUNCF7C',
    'ADMINISTRADOR'
);
