CREATE TABLE uploads
(
    id                  SERIAL UNIQUE NOT NULL,
    filename            VARCHAR(255) NOT NULL,
    data                BYTEA NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                  SERIAL UNIQUE NOT NULL,
    email               VARCHAR(255) UNIQUE NOT NULL,
    password            VARCHAR(255) NOT NULL,
    role                VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE locations
(
    id                  SERIAL UNIQUE NOT NULL,
    name                VARCHAR(255) UNIQUE NOT NULL,
    icon_id             BIGINT NOT NULL,
    model3d_id          BIGINT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (icon_id) REFERENCES uploads (id),
    FOREIGN KEY (model3d_id) REFERENCES uploads (id)
);

CREATE TABLE sensors
(
    id                  SERIAL UNIQUE NOT NULL,
    ttn_id              VARCHAR(255) UNIQUE NOT NULL,
    location_id         BIGINT DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE user_locations_with_permission
(
    user_id             BIGINT,
    location_id         BIGINT,

    PRIMARY KEY (user_id, location_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (location_id) REFERENCES locations (id)
);
