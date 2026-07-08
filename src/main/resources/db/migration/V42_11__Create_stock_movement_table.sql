create table if not exists stock_movement
(
    "stock-movement-uuid" uuid not null
        constraint stock_movement_pk primary key,
    "book-uuid"           uuid not null
        constraint stock_movement_book_fk references book,
    "movement-type"       varchar not null,
    "movement-date"       timestamp not null,
    quantity              integer not null,
    "sale-uuid"           uuid
        constraint stock_movement_sale_fk references sale,
    reason                varchar
);
