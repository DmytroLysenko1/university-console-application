DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'university') THEN
            PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE university WITH ENCODING = ''UTF8''');
        END IF;
    END
$$;