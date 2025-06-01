CREATE SCHEMA admin_register;

CREATE EXTENSION IF NOT EXISTS hstore WITH SCHEMA public;


CREATE SEQUENCE IF NOT EXISTS admin_register.admin_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE admin_register.admin_data
(
    id integer DEFAULT nextval('admin_register.admin_data_id_seq'),
    name character varying(100) COLLATE pg_catalog."default",
    email character varying(100) COLLATE pg_catalog."default",
    phone character varying(15) COLLATE pg_catalog."default",
    gender character(1) COLLATE pg_catalog."default",
    dob character varying(50) COLLATE pg_catalog."default",
    bank_details text COLLATE pg_catalog."default",
    address text COLLATE pg_catalog."default",
    company_details text COLLATE pg_catalog."default",
    created_at timestamp(0) without time zone DEFAULT CURRENT_TIMESTAMP(0),
    email_verified boolean DEFAULT false,
    deleted boolean DEFAULT false,
    CONSTRAINT admin_data_pkey PRIMARY KEY (id)
);

ALTER SEQUENCE admin_register.admin_data_id_seq OWNED BY admin_register.admin_data.id;


INSERT INTO admin_register.admin_data (
    name,
    email,
    phone,
    gender,
    dob,
    bank_details,
    address,
    company_details,
    created_at,
    email_verified,
    deleted
) VALUES (
    'John Doe',
    'name@gmail.com',
    '1234567890',
    'M',
    '1990-01-01',
    'Bank details',
    'Address',
    'Company details',
    CURRENT_TIMESTAMP,
    false,
    false
);
