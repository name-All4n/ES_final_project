DO $$
BEGIN
    IF (SELECT data_type FROM information_schema.columns
        WHERE table_name = 'medicos' AND column_name = 'uf') = 'character' THEN
ALTER TABLE medicos ALTER COLUMN uf TYPE VARCHAR(2);
END IF;

    IF (SELECT data_type FROM information_schema.columns
        WHERE table_name = 'pacientes' AND column_name = 'uf') = 'character' THEN
ALTER TABLE pacientes ALTER COLUMN uf TYPE VARCHAR(2);
END IF;
END $$;