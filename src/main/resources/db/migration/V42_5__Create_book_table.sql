create table if not exists book
(
    "book-uuid"       uuid not null
        constraint book_pk primary key,
    title             varchar not null,
    genre             varchar,
    "publication-date" date,
    "purchase-price"  numeric(10,2),
    "selling-price"   numeric(10,2),
    "current-stock"   integer default 0,
    "category-uuid"   uuid
        constraint book_category_fk references category
);
