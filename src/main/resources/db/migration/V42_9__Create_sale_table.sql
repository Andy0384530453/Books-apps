create table if not exists sale
(
    "sale-uuid"      uuid not null
        constraint sale_pk primary key,
    "sale-date"      timestamp not null,
    "payment-status" varchar not null,
    "customer-uuid"  uuid not null
        constraint sale_customer_fk references customer
);
