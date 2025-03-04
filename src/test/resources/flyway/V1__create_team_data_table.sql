CREATE SCHEMA employee_message;


CREATE EXTENSION IF NOT EXISTS hstore WITH SCHEMA public;

--DROP TABLE employee_message.team_data;

-- Create the sequence explicitly
CREATE SEQUENCE IF NOT EXISTS employee_message.team_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE employee_message.team_data
(
    id integer DEFAULT nextval('employee_message.team_data_id_seq'),
    name character varying(255) NOT NULL,
    manager_emp_id character varying(50) ,
    team_count integer,
    team_members text[],
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    deleted boolean,
    is_active boolean,
    CONSTRAINT team_data_pkey PRIMARY KEY (id),
    CONSTRAINT team_data_managerempid_key UNIQUE (manager_emp_id)
);



-- Link the sequence to the id column (if not already done by default)
ALTER SEQUENCE employee_message.team_data_id_seq OWNED BY employee_message.team_data.id;


INSERT INTO employee_message.team_data(
	name, manager_emp_id, team_count, team_members, created_at, updated_at, deleted, is_active)
	VALUES ( 'team 1', 'PC0', 0, '{}', '2024-12-28 13:36:48.662867+05:30', '2025-01-04 20:50:33.993502+05:30', false, true);

	INSERT INTO employee_message.team_data (
        name, manager_emp_id, team_count, team_members, created_at, updated_at, deleted, is_active
    ) VALUES (
        'team 2',
        'PK0',
        0,
        '{JK0}',  -- Assuming team_members is of type JSONB
        '2025-02-16 17:37:59',
        '2025-02-16 17:37:59',
        false,
        true
    );
