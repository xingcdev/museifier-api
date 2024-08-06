DROP TABLE IF EXISTS visit;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS museum;
-- Password hashing functions
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE account (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    username varchar(255),
    password text
);

CREATE TABLE museum (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255),
    address varchar(255),
    postal_code varchar(255),
    city varchar(255),
    department varchar(255),
    phone_number varchar(255),
    url varchar(255),
    latitude double precision,
    longitude double precision
);

CREATE TABLE visit (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    comment varchar(255),
    museum_id uuid REFERENCES museum NOT NULL,
    user_id varchar(255) NOT NULL
);