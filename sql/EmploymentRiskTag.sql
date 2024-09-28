drop table employment_risk_tag;

create table employment_risk_tag (
    id int primary key auto_increment,
    employment_id int,
    risk_tag_id int
);

insert into employment_risk_tag (employment_id, risk_tag_id) values (1, 1);
insert into employment_risk_tag (employment_id, risk_tag_id) values (1, 2);
insert into employment_risk_tag (employment_id, risk_tag_id) values (1, 3);
insert into employment_risk_tag (employment_id, risk_tag_id) values (2, 4);
insert into employment_risk_tag (employment_id, risk_tag_id) values (2, 5);
insert into employment_risk_tag (employment_id, risk_tag_id) values (3, 6);
insert into employment_risk_tag (employment_id, risk_tag_id) values (4, 5);