CREATE TABLE products (
    id uuid PRIMARY KEY,
    name varchar(150) not null,
    available_quantity integer not null,
    reserved_quantity integer not null,
    created_at timestamp with time zone not null ,
    updated_at timestamp with time zone not null ,

    constraint chk_products_available_quantity
                      check ( available_quantity >= 0 ),

    constraint chk_products_reserved_quantity
                      check ( reserved_quantity >= 0 )
);

CREATE INDEX idx_products_name
on products(name);