drop table risk_tag;

create table risk_tag (
    id int primary key auto_increment,
    name char(8)
);

insert into risk_tag (name) values ('高处');
insert into risk_tag (name) values ('动火');
insert into risk_tag (name) values ('触电');
insert into risk_tag (name) values ('高坠');
insert into risk_tag (name) values ('窒息');
insert into risk_tag (name) values ('中毒');
