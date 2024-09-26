drop table User;

create table User (
    id INT PRIMARY KEY,
    username CHAR(16),
    password CHAR(16),
    level TINYINT,
    depart_id INT
);

insert into User VALUES (0, 'root', '123456', 2, -1);
insert into User VALUES (1, 'test0', '123456', 1, 0);
insert into User VALUES (2, 'test1', '123456', 1, 0);
insert into User VALUES (3, 'test2', '123456', 1, 1);
