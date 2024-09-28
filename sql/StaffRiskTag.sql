drop table staff_risk_tag;

create table staff_risk_tag (
    id int primary key auto_increment,
    staff_id int,
    risk_tag_id int
);

insert into staff_risk_tag (staff_id, risk_tag_id) values (1, 1);
insert into staff_risk_tag (staff_id, risk_tag_id) values (1, 2);
insert into staff_risk_tag (staff_id, risk_tag_id) values (1, 3);
insert into staff_risk_tag (staff_id, risk_tag_id) values (2, 4);
insert into staff_risk_tag (staff_id, risk_tag_id) values (2, 5);
insert into staff_risk_tag (staff_id, risk_tag_id) values (3, 6);
insert into staff_risk_tag (staff_id, risk_tag_id) values (4, 5);