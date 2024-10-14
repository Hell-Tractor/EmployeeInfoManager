drop table staff;

create table staff (
    id INT primary key AUTO_INCREMENT,
    name CHAR(6),
    image CHAR(32),
    born_year INT,
    person_id CHAR(18),
    experience varchar(128),
    physical_condition varchar(128),
    appendix varchar(128),
    INDEX person_id_index (person_id)
);

insert into staff (name, image, born_year, person_id, experience, physical_condition, appendix)VALUES ('张三', 'test_image', 1985, '142700198501010000', 'experience', 'physical_condition', 'appendix');
insert into staff (name, image, born_year, person_id, experience, physical_condition, appendix)VALUES ('李四', 'test_image', 1986, '142700198601010000', 'experience', 'physical_condition', 'appendix');
insert into staff (name, image, born_year, person_id, experience, physical_condition, appendix)VALUES ('王五', 'test_image', 1987, '142700198701010000', 'experience', 'physical_condition', 'appendix');
insert into staff (name, image, born_year, person_id, experience, physical_condition, appendix)VALUES ('张伟', 'test_image', 1983, '142700198301010000', 'experience', 'physical_condition', 'appendix');
