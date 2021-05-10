CREATE TABLE uploads
(
    id                  SERIAL UNIQUE NOT NULL,
    filename            VARCHAR(256) NOT NULL,
    data                BYTEA NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE users
(
    username            VARCHAR(256) UNIQUE NOT NULL,
    password            VARCHAR(256) NOT NULL,
    role                VARCHAR(256) NOT NULL,

    PRIMARY KEY (username)
);

CREATE TABLE locations
(
    id                  SERIAL UNIQUE NOT NULL,
    name                VARCHAR(256) UNIQUE NOT NULL,
    icon_id             BIGINT NOT NULL,
    model3d_id          BIGINT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (icon_id) REFERENCES uploads (id),
    FOREIGN KEY (model3d_id) REFERENCES uploads (id)
);

CREATE TABLE sensors
(
    id                  SERIAL UNIQUE NOT NULL,
    name                VARCHAR(256),
    ttn_id              VARCHAR(256) UNIQUE,
    location_id         BIGINT DEFAULT NULL,
    location_positionx  FLOAT DEFAULT 0,
    location_positiony  FLOAT DEFAULT 0,
    location_positionz  FLOAT DEFAULT 0,
    location_rotationx  FLOAT DEFAULT 0,
    location_rotationy  FLOAT DEFAULT 0,
    location_rotationz  FLOAT DEFAULT 0,

    PRIMARY KEY (id),
    FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE user_locations_with_permission
(
    user_username       VARCHAR(256) NOT NULL,
    location_id         BIGINT NOT NULL,

    PRIMARY KEY (user_username, location_id),
    FOREIGN KEY (user_username) REFERENCES users (username),
    FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE oauth_access_token
(
    token_id            VARCHAR(256),
    token               BYTEA,
    authentication_id   VARCHAR(256),
    user_name           VARCHAR(256),
    client_id           VARCHAR(256),
    authentication      BYTEA,
    refresh_token       VARCHAR(256)
);

CREATE TABLE oauth_refresh_token
(
    token_id           VARCHAR(256),
    token              BYTEA,
    authentication     BYTEA
);
