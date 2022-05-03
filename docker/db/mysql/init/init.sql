CREATE TABLE game
(
    id    BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    opponent_password VARCHAR(100),
    running BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE event
(
    game_id  BIGINT NOT NULL,
    type  VARCHAR(20) NOT NULL,
    description VARCHAR(20)
);
