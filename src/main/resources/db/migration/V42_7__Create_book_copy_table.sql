create table if not exists book_copy
(
    "book-copy-uuid" uuid not null
        constraint book_copy_pk primary key,
    "book-uuid"      uuid not null
        constraint book_copy_book_fk references book,
    format           varchar not null,
    quantity         integer not null
);
