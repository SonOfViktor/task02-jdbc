CREATE TABLE task02.positions
(
    id bigserial primary key,
    name varchar(50) not null unique
);

CREATE TABLE task02.projects
(
    id bigserial primary key,
    name varchar(50) not null unique
);

CREATE TABLE task02.employees
(
    id bigserial primary key,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    position_id bigint not null default 1,
    foreign key (position_id) references task02.positions (id)
        ON DELETE set default
        ON UPDATE CASCADE
);

CREATE TABLE task02.employee_projects
(
    employee_id bigint not null,
    project_id bigint not null,
    PRIMARY KEY (employee_id, project_id),
    FOREIGN KEY (employee_id) REFERENCES task02.employees(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (project_id) REFERENCES task02.projects(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);