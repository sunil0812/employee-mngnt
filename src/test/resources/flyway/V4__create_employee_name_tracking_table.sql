
-- Create the sequence explicitly
CREATE SEQUENCE IF NOT EXISTS employee_message.employees_name_tracking_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS employee_message.employees_name_tracking
(
   id bigint DEFAULT nextval('employee_message.employees_name_tracking_id_seq'),
    first_name character varying(100) ,
    name_count integer NOT NULL DEFAULT 1,
        CONSTRAINT employees_name_tracking_pkey PRIMARY KEY (id)
);




-- Link the sequence to the id column (if not already done by default)
ALTER SEQUENCE employee_message.employees_name_tracking_id_seq OWNED BY employee_message.employees_name_tracking.id;


