create table users
(
    id                bigint primary key AUTO_INCREMENT,
    username          varchar(50)  not null,
    name              varchar(50)  not null,
    surname           varchar(100) not null,
    password          varchar(60),
    email             varchar(255) unique,
    locale            varchar(5),
    enabled           boolean,
    registration_date timestamp    not null,
    last_login_date   timestamp
);

create table user_roles
(
    id      bigint primary key AUTO_INCREMENT,
    user_id bigint      not null,
    role    VARCHAR(16) not null,
    foreign key (user_id) references users (id) on delete cascade
);

create index idx_user_roles_user_id on user_roles (user_id);