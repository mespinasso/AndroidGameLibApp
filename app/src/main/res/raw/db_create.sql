CREATE TABLE sys_user (
    username varchar(255) NOT NULL PRIMARY KEY,
    password varchar(255) NOT NULL,
    name varchar(255)
);

CREATE TABLE library_game (
    game_id int NOT NULL PRIMARY KEY,
    rating double,
    notes varchar(500)
);