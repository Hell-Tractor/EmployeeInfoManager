drop table staff;

create table staff (
    id int primary key auto_increment,
    name char(5),
    image char(16),
    born_year int,
    person_id char(18),
    depart_id int,
    project char(16),
    valid_since date,
    valid_until date,
    work_permit char(16),
    experience varchar(128),
    physical_condition varchar(128),
    violation varchar(128),
    appendix varchar(128)
);

insert into staff (name, image, born_year, person_id, depart_id, project, valid_since, valid_until, work_permit, experience, physical_condition, violation, appendix) VALUES ('张三', 'test_image', 1985, '142700198501010000', 1, 'test', '2024-09-01', '2025-09-01', 'test_permit', 'experience', 'physical', 'violation', 'appendix');
insert into staff (name, image, born_year, person_id, depart_id, project, valid_since, valid_until, work_permit, experience, physical_condition, violation, appendix) VALUES ('李四', 'test_image', 1986, '142700198601010000', 1, 'test', '2020-09-01', '2021-09-01', 'test_permit', 'experience', 'physical', 'violation', 'appendix');
insert into staff (name, image, born_year, person_id, depart_id, project, valid_since, valid_until, work_permit, experience, physical_condition, violation, appendix) VALUES ('王五', 'test_image', 1987, '142700198701010000', 1, 'test', '2025-09-01', '2026-09-01', 'test_permit', 'experience', 'physical', 'violation', 'appendix');
insert into staff (name, image, born_year, person_id, depart_id, project, valid_since, valid_until, work_permit, experience, physical_condition, violation, appendix) VALUES ('张伟', 'test_image', 1983, '142700198301010000', 2, 'test', '2024-09-01', '2025-09-01', 'test_permit', 'experience', 'physical', 'violation', 'appendix');
