create table if not exists customer
(
    "customer-uuid" uuid not null
        constraint customer_pk primary key,
    "first-name"    varchar not null,
    "last-name"     varchar not null,
    "phone-number"  varchar,
    email           varchar
);
