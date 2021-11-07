create table if not exists city (
    id int primary key,
    name varchar,
    region varchar,
    district varchar,
    population int,
    foundation int
);

create sequence sql_sequence_city_id start with 1 increment by 1;