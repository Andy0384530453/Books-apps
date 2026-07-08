create table if not exists sale_book
(
    "sale-book-uuid" uuid not null
        constraint sale_book_pk primary key,
    "sale-uuid"      uuid not null
        constraint sale_book_sale_fk references sale,
    "book-uuid"      uuid not null
        constraint sale_book_book_fk references book,
    quantity         integer not null
);
