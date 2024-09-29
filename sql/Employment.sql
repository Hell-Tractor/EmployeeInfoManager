drop table employment;

create table employment (
    id int primary key auto_increment,
    staff_id int,
    depart_id int,
    project char(16),
    valid_since date,
    valid_until date,
    work_permit char(16),
    violation varchar(128)
);

insert into employment (staff_id, depart_id, project, valid_since, valid_until, work_permit, violation) VALUES (1, 1, 'test', '2024-09-01', '2025-09-01', 'test_permit', 'violation');
insert into employment (staff_id, depart_id, project, valid_since, valid_until, work_permit, violation) VALUES (2, 1, 'test', '2020-09-01', '2021-09-01', 'test_permit', 'violation');
insert into employment (staff_id, depart_id, project, valid_since, valid_until, work_permit, violation) VALUES (3, 1, 'test', '2025-09-01', '2026-09-01', 'test_permit', 'violation');
insert into employment (staff_id, depart_id, project, valid_since, valid_until, work_permit, violation) VALUES (4, 2, 'test', '2024-09-01', '2025-09-01', 'test_permit', 'violation');
insert into employment (staff_id, depart_id, project, valid_since, valid_until, work_permit, violation) VALUES (1, 1, 'test', '2023-09-01', '2024-08-01', 'test_permit', 'violation2');
