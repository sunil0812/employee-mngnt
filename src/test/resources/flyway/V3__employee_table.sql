
CREATE TABLE employee_message.employees_data
(
    emp_id character varying(250) NOT NULL,
    name character varying(100),
    role character varying(50) ,
    email character varying(100) ,
    phone character varying(15) ,
    gender character(1) ,
    dob character varying(50) ,
    emp_type character varying(5) ,
    bank_details text,
    address text ,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone,
    team_id integer,
    is_active boolean DEFAULT true,
    deleted boolean,
    CONSTRAINT employees_data_pkey PRIMARY KEY (emp_id),
    CONSTRAINT employees_data_empid_key UNIQUE (emp_id),
    CONSTRAINT fk_team FOREIGN KEY (team_id)
        REFERENCES employee_message.team_data (id) MATCH SIMPLE
);

CREATE OR REPLACE FUNCTION set_is_active()
RETURNS TRIGGER AS $$
BEGIN
    -- Set the is_active column to true for the new row
    NEW.is_active := TRUE;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_set_is_active
BEFORE INSERT ON employee_message.employees_data
FOR EACH ROW
EXECUTE FUNCTION set_is_active();



INSERT INTO employee_message.employees_data (
    emp_id, name, role, email, phone, gender, dob, emp_type,
    bank_details, address, created_at, updated_at, team_id, is_active, deleted
) VALUES (
    'PK0',
    'pati k',
    'MANAGER',
    'pati234@gmail.com',
    '+91 8231562893',
    'M',
    '2024-12-02',  -- YYYY-MM-DD format
    'F',
    '{"accountNo":"1234567890","ifscCode":"XYZB0001234"}',
    '{"no":"1","street":"123 Main St","pinCode":"62704","country":"USA"}',
    '2025-02-16 17:37:59',
    '2025-02-16 17:37:59',
    2,
    true,
    false
);

INSERT INTO employee_message.employees_data (
    emp_id, name, role, email, phone, gender, dob, emp_type,
    bank_details, address, created_at, updated_at, team_id, is_active, deleted
) VALUES (
    'JK0',
    'jhon k',
    'SOFTWARE_ENGINEER',
    'jhon234@gmail.com',
    '+91 8631562893',
    'M',
    '2024-12-02',  -- YYYY-MM-DD format
    'F',
    '{"accountNo":"1234567890","ifscCode":"XYZB0001234"}',
    '{"no":"1","street":"123 Main St","pinCode":"62704","country":"USA"}',
    '2025-02-16 17:37:59',
    '2025-02-16 17:37:59',
    2,
    true,
    false
);






















