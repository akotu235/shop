create table categories
(
    id   bigint primary key AUTO_INCREMENT,
    name varchar(255) not null unique
);

create table products
(
    id                 bigint primary key AUTO_INCREMENT,
    name               varchar(255) not null,
    description        varchar(511) null,
    available_quantity int,
    photos_quantity    int,
    price              float(53)    not null,
    currency           varchar(16)  not null,
    enabled            boolean,
    category_id        bigint       not null,
    constraint fk_category
        foreign key (category_id)
            references categories (id)
            on delete cascade
);

create table orders
(
    id                bigint primary key AUTO_INCREMENT,
    user_id           bigint      not null,
    status            varchar(32) not null,
    delivery_method   varchar(32),
    submission_date   timestamp null,
    modification_date timestamp null,
    constraint fk_user
        foreign key (user_id)
            references users (id)
            on delete cascade
);

create table order_positions
(
    id         bigint primary key AUTO_INCREMENT,
    order_id   bigint not null,
    product_id bigint not null,
    quantity   int    not null,
    constraint fk_order
        foreign key (order_id)
            references orders (id)
            on delete cascade,
    constraint fk_product
        foreign key (product_id)
            references products (id)
            on delete cascade
);

create table shipping_details
(
    id          bigint primary key AUTO_INCREMENT,
    order_id    bigint not null,
    country     varchar(100),
    street     varchar(255),
    city        varchar(255),
    postal_code varchar(20),
    phone       varchar(20),
    constraint fk_shipping_order foreign key (order_id) references orders (id) on delete cascade
);