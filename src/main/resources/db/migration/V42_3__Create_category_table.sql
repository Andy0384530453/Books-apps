create table if not exists category
(
    "category-uuid" uuid not null
        constraint category_pk primary key,
    "category-name" varchar not null
);
