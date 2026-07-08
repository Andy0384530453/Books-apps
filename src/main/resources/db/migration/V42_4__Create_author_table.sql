create table if not exists author
(
    "author-uuid" uuid not null
        constraint author_pk primary key,
    "last-name"   varchar not null,
    "first-name"  varchar not null
);
