DROP TABLE IF EXISTS visit;
DROP TABLE IF EXISTS museum;
CREATE EXTENSION IF NOT EXISTS unaccent;

CREATE TABLE museum
(
    id           uuid default gen_random_uuid() PRIMARY KEY,
    name         varchar(255),
    address      varchar(255),
    postal_code  varchar(255),
    city         varchar(255),
    department   varchar(255),
    phone_number varchar(255),
    url          varchar(255),
    latitude     double precision,
    longitude    double precision
);

CREATE TABLE visit
(
    id         uuid default gen_random_uuid() PRIMARY KEY,
    title      varchar(50)            NOT NULL,
    visit_date date                   NOT NULL,
    comment    varchar(255)           NOT NULL,
    rating     integer                NOT NULL,
    museum_id  uuid REFERENCES museum NOT NULL,
    user_id    varchar(255)           NOT NULL,
    created    timestamp,
    updated    timestamp
);