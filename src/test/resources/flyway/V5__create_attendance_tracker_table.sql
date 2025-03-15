
CREATE TABLE IF NOT EXISTS employee_message.attendance_details
(
    emp_id character varying COLLATE pg_catalog."default" NOT NULL,
    m_emp_id character varying COLLATE pg_catalog."default" NOT NULL,
    team_id integer NOT NULL,
    wfo integer DEFAULT 0,
    wfh integer DEFAULT 0,
    ooo integer DEFAULT 0,
    created_at timestamp(0) without time zone DEFAULT CURRENT_TIMESTAMP(0),
    updated_at timestamp(0) without time zone DEFAULT CURRENT_TIMESTAMP(0),
    CONSTRAINT attendance_details_pkey PRIMARY KEY (emp_id)
);

INSERT INTO employee_message.attendance_details(
	emp_id, m_emp_id, team_id, wfo, wfh, ooo, created_at, updated_at)
	VALUES ('MK0', 'FK0', 3, 0, 1, 0, CURRENT_DATE, CURRENT_DATE);