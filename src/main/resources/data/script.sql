create table customer_key
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    created_by varchar(255) null,
    updated_at datetime(6)  null,
    updated_by varchar(255) null,
    active     bit          not null,
    alias      varchar(255) null,
    cid        bigint       null,
    issued     datetime(6)  null,
    key_proxy  varchar(255) null,
    outdated   datetime(6)  null,
    price      int          not null,
    type       varchar(255) null
);

create table discount
(
    id       int          not null
        primary key,
    percent  int          not null,
    quantity int          not null,
    total    int          not null,
    type     varchar(255) null
);

create table in_use_proxy
(
    key_proxy varchar(255) not null
        primary key,
    device_id varchar(255) null,
    out_date  datetime(6)  null,
    port      varchar(255) null
);

create table orders
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    created_by varchar(255) null,
    updated_at datetime(6)  null,
    updated_by varchar(255) null,
    amount     int          not null,
    cid        bigint       null,
    content    varchar(255) null,
    key_type   varchar(255) null,
    order_code bigint       null,
    pid        bigint       null,
    quantity   int          not null,
    status     varchar(255) null,
    times      int          not null
);

create table payos_acc
(
    code          int          not null
        primary key,
    avatar_url    varchar(255) null,
    email         varchar(255) null,
    expires_at    bigint       null,
    first_name    varchar(255) null,
    id            varchar(255) null,
    last_name     varchar(255) null,
    refresh_token varchar(255) null,
    token         varchar(255) null
);

create table product
(
    id          bigint       not null
        primary key,
    created_at  datetime(6)  null,
    created_by  varchar(255) null,
    updated_at  datetime(6)  null,
    updated_by  varchar(255) null,
    description varchar(255) null,
    name        varchar(255) null,
    price       int          not null,
    type        varchar(255) null
);

create table product_seq
(
    next_val bigint null
);

create table proxies
(
    id                varchar(255) not null
        primary key,
    auth              varchar(255) null,
    eth_port          varchar(255) null,
    http_local        varchar(255) null,
    http_remote       varchar(255) null,
    ip_public         varchar(255) null,
    last_link_up_time varchar(255) null,
    mtu               varchar(255) null,
    sock_local        varchar(255) null,
    sock_remote       varchar(255) null,
    status            varchar(255) null,
    total_rx          varchar(255) null,
    total_tx          varchar(255) null
);

create table roles
(
    id   int auto_increment
        primary key,
    name varchar(255) null
);

create table users
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    created_by varchar(255) null,
    updated_at datetime(6)  null,
    updated_by varchar(255) null,
    password   varchar(255) null,
    username   varchar(255) null,
    wallet     int          not null
);

create table users_roles
(
    user_id bigint not null,
    role_id int    not null,
    primary key (user_id, role_id),
    constraint FK2o0jvgh89lemvvo17cbqvdxaa
        foreign key (user_id) references users (id),
    constraint FKj6m8fwv7oqv74fcehir1a9ffy
        foreign key (role_id) references roles (id)
);

