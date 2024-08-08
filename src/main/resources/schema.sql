DROP TABLE IF EXISTS visit;
DROP TABLE IF EXISTS museum;

CREATE TABLE museum
(
    id           uuid PRIMARY KEY,
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
    id        uuid PRIMARY KEY,
    comment   varchar(255),
    museum_id uuid REFERENCES museum NOT NULL,
    user_id   varchar(255)           NOT NULL
);