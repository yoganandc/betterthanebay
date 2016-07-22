-- A script to create employee table
create table employees(
    -- auto-generated primary key
    id bigint primary key not null auto_increment,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    username varchar(255) not null,
    password varchar(255) not null;
    addressId varchar(255) not null;
    paymentId  varchar(255) not null,
    ratingId varchar(255) not null
);