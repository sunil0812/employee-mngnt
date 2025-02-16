CREATE EXTENSION IF NOT EXISTS hstore WITH SCHEMA public;

-- Create the sequence explicitly
CREATE SEQUENCE IF NOT EXISTS employee_message.manager_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE employee_message.manager_data
(
    id integer DEFAULT nextval('employee_message.manager_data_id_seq'),
    name character varying(255) NOT NULL,
    team_name character varying(255)  NOT NULL,
    emp_id character varying(50),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    deleted boolean,
    is_active boolean,
    CONSTRAINT manager_data_pkey PRIMARY KEY (id),
    CONSTRAINT manager_data_empid_key UNIQUE (emp_id)
);


-- Link the sequence to the id column (if not already done by default)
ALTER SEQUENCE employee_message.manager_data_id_seq OWNED BY employee_message.manager_data.id;

INSERT INTO employee_message.manager_data (
    id, name, team_name, emp_id, created_at, updated_at, deleted, is_active
) VALUES (
    5,
    'pati k',
    'team 2',
    'PK0',
    NOW(),  -- Use current timestamp if created_at is missing
    NOW(),  -- Use current timestamp if updated_at is missing
    false,
    true
);

