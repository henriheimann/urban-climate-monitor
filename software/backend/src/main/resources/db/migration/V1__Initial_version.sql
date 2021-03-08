CREATE TABLE users
(
    email               VARCHAR(255) UNIQUE NOT NULL,
    password            VARCHAR(255) NOT NULL,
    role                VARCHAR(255) NOT NULL,

    PRIMARY KEY (email)
);

CREATE TABLE locations
(
    id                  VARCHAR(255) UNIQUE NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE devices
(
    id                  VARCHAR(255) UNIQUE NOT NULL,
    location_id         VARCHAR(255) NOT NULL DEFAULT 'unassigned',

    PRIMARY KEY (id),
    FOREIGN KEY (location_id) REFERENCES locations (id)
);
