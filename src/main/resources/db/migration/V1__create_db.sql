DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM  pg_catalog WHERE datname = 'university') THEN
            CREATE DATABASE university WITH ENCODING = 'UTF8';
        END IF;
    END
$$;
