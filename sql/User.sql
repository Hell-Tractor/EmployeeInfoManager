drop table user;

create table user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username CHAR(16),
    password CHAR(16),
    level TINYINT,
    salt CHAR(8) DEFAULT NULL,
    depart_id INT
);

insert into user(username, password, level, depart_id) VALUES ('root', '123456', 2, -1);
insert into user(username, password, level, depart_id) VALUES ('test0', '123456', 1, 0);
insert into user(username, password, level, depart_id) VALUES ('test1', '123456', 1, 0);
insert into user(username, password, level, depart_id) VALUES ('test2', '123456', 1, 1);
