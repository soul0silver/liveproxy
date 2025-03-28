create database proxy_server;

create table customer_key
(
    id         bigint auto_increment
        primary key,
    cid        bigint                       not null,
    key_proxy  varchar(1000)                not null,
    issued     timestamp                    not null,
    created_by varchar(255)                 null,
    updated_at timestamp                    not null,
    updated_by varchar(255)                 null,
    created_at timestamp                    not null,
    outdated   datetime(6)                  null,
    active     bit                          not null,
    type       varchar(50)                  null,
    alias      varchar(255) charset utf8mb3 null,
    price      int                          null
);

create table key_seq
(
    next_val bigint null
);

create table orders
(
    id         bigint auto_increment
        primary key,
    amount     double       not null,
    cid        bigint       null,
    key_type   varchar(255) null,
    order_code bigint       null,
    pid        bigint       null,
    status     varchar(255) null,
    created_by varchar(255) null,
    updated_at timestamp    not null,
    updated_by varchar(255) null,
    created_at timestamp    not null,
    content    varchar(255) null,
    quantity   int          null,
    times      int          null
);

create table product
(
    id          bigint auto_increment
        primary key,
    name        varchar(255) null,
    price       double       null,
    created_by  varchar(255) null,
    type        varchar(255) null,
    updated_at  timestamp    not null,
    updated_by  varchar(255) null,
    created_at  timestamp    not null,
    description varchar(255) null
);

create table product_seq
(
    next_val bigint null
);

create table proxies
(
    id         bigint auto_increment
        primary key,
    ip         varchar(255) null,
    location   varchar(255) null,
    port       varchar(255) null,
    status     int          not null,
    created_by varchar(255) null,
    type       varchar(50)  not null,
    updated_at timestamp    not null,
    updated_by varchar(255) null,
    created_at timestamp    not null,
    key_id     bigint       null
);

create table roles
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);

create table users
(
    id         bigint auto_increment
        primary key,
    password   varchar(255) null,
    created_by varchar(255) null,
    username   varchar(255) null,
    updated_at timestamp    not null,
    updated_by varchar(255) null,
    created_at timestamp    not null,
    wallet     int          not null
);

create table users_roles
(
    id      bigint auto_increment
        primary key,
    user_id bigint null,
    role_id int    not null
);



