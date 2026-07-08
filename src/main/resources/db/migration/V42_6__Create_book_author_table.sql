create table if not exists book_author
(
    "book-uuid"   uuid not null
        constraint book_author_book_fk references book,
    "author-uuid" uuid not null
        constraint book_author_author_fk references author,
    constraint book_author_pk primary key ("book-uuid", "author-uuid")
);
