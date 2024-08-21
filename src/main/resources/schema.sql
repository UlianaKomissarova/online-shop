--Для удаления всех объектов таблиц и обновления id:
--ALTER SEQUENCE user_sequence RESTART WITH 1;
--ALTER SEQUENCE item_sequence RESTART WITH 1;
--ALTER SEQUENCE delivery_sequence RESTART WITH 1;
--ALTER SEQUENCE order_sequence RESTART WITH 1;
--ALTER SEQUENCE order_item_sequence RESTART WITH 1;
--
--TRUNCATE users RESTART IDENTITY CASCADE;
--TRUNCATE items RESTART IDENTITY CASCADE;
--TRUNCATE orders RESTART IDENTITY CASCADE;
--TRUNCATE orders_items RESTART IDENTITY CASCADE;
--TRUNCATE post_deliveries RESTART IDENTITY CASCADE;
--TRUNCATE parcel_locker_deliveries RESTART IDENTITY CASCADE;
--TRUNCATE courier_deliveries RESTART IDENTITY CASCADE;
--TRUNCATE deliveries RESTART IDENTITY CASCADE;

create sequence user_sequence start 1;
create sequence item_sequence start 1;
create sequence delivery_sequence start 1;
create sequence order_sequence start 1;
create sequence order_item_sequence start 1;

create TABLE IF NOT EXISTS users (
	user_id INTEGER NOT NULL DEFAULT nextval('user_sequence'),
	name VARCHAR NOT NULL,
	email VARCHAR NOT NULL unique,
	phone CHAR(12) NOT NULL,
	birthday DATE,
	is_vendor BOOLEAN NOT NULL,
	CONSTRAINT pk_users PRIMARY KEY (user_id),
	CONSTRAINT uq_user_email UNIQUE (email)
);

create TABLE IF NOT EXISTS items (
    item_id INTEGER NOT NULL DEFAULT nextval('item_sequence'),
    name VARCHAR NOT NULL,
    description VARCHAR,
    price NUMERIC NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    is_available BOOLEAN NOT NULL,
    vendor_id INTEGER,
    CONSTRAINT pk_items PRIMARY KEY (item_id),
    CONSTRAINT fk_items_users FOREIGN KEY (vendor_id) REFERENCES users (user_id)
);

create TABLE IF NOT EXISTS orders (
    order_id INTEGER NOT NULL DEFAULT nextval('order_sequence'),
    buyer_id INTEGER NOT NULL,
    status VARCHAR(15),
    created DATE,
    CONSTRAINT pk_orders PRIMARY KEY (order_id),
    CONSTRAINT fk_orders_users FOREIGN KEY (buyer_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS orders_items (
    id INTEGER NOT NULL DEFAULT nextval('order_item_sequence'),
    order_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    CONSTRAINT pk_orders_items PRIMARY KEY (id),
    CONSTRAINT fk_orders_items_orders FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT fk_orders_items_items FOREIGN KEY (item_id) REFERENCES items (item_id)
);

CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id INTEGER NOT NULL DEFAULT nextval('delivery_sequence'),
    order_id INTEGER NOT NULL,
    address VARCHAR NOT NULL,
    price NUMERIC,
    delivered_on DATE,
    CONSTRAINT pk_deliveries PRIMARY KEY (delivery_id),
    CONSTRAINT fk_deliveries_orders FOREIGN KEY (order_id) REFERENCES orders (order_id)
);

CREATE TABLE IF NOT EXISTS post_deliveries (
    delivery_id INTEGER NOT NULL DEFAULT nextval('delivery_sequence'),
    track_number CHAR(14) NOT NULL,
    CONSTRAINT pk_post_deliveries PRIMARY KEY (delivery_id),
    CONSTRAINT fk_post_deliveries_deliveries FOREIGN KEY (delivery_id) REFERENCES deliveries (delivery_id)
);

CREATE TABLE IF NOT EXISTS parcel_locker_deliveries (
    delivery_id INTEGER NOT NULL DEFAULT nextval('delivery_sequence'),
    box_number INTEGER NOT NULL,
    pin CHAR(4) NOT NULL,
    open_from TIME,
    closed_from TIME,
    CONSTRAINT pk_parcel_locker_deliveries PRIMARY KEY (delivery_id),
    CONSTRAINT fk_parcel_locker_deliveries_deliveries FOREIGN KEY (delivery_id) REFERENCES deliveries (delivery_id)
);

CREATE TABLE IF NOT EXISTS courier_deliveries (
    delivery_id INTEGER NOT NULL DEFAULT nextval('delivery_sequence'),
    deliver_from TIME,
    deliver_to TIME,
    courier_phone VARCHAR(15),
    CONSTRAINT pk_courier_deliveries PRIMARY KEY (delivery_id),
    CONSTRAINT fk_courier_deliveries_deliveries FOREIGN KEY (delivery_id) REFERENCES deliveries (delivery_id)
);