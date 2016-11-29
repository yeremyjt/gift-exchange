USE GiftExchange;

DROP TABLE IF EXISTS exchange_history;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS person_type;
DROP TABLE IF EXISTS family;
DROP TABLE IF EXISTS family_group;

CREATE TABLE family_group (
  name VARCHAR(36),
  PRIMARY KEY (name)
) ENGINE = INNODB;

CREATE TABLE family (
  name              VARCHAR(36),
  family_group_name VARCHAR(36),
  PRIMARY KEY (name),
  FOREIGN KEY (family_group_name) REFERENCES family_group (name)
) ENGINE = INNODB;

CREATE TABLE person_type (
  type VARCHAR(36),
  PRIMARY KEY (type)
) ENGINE = INNODB;

CREATE TABLE person (
  id          INTEGER AUTO_INCREMENT,
  name        VARCHAR(36),
  type        VARCHAR(36),
  family_name VARCHAR(36),
  PRIMARY KEY (id),
  FOREIGN KEY (type) REFERENCES person_type (type),
  FOREIGN KEY (family_name) REFERENCES family (name)
) ENGINE = INNODB;

CREATE TABLE exchange_history (
  id       INTEGER AUTO_INCREMENT,
  giver_id    INTEGER,
  giver_name VARCHAR(36),
  receiver_id INTEGER,
  receiver_name VARCHAR(36),
  family_group VARCHAR(36),
  year     INTEGER,
  PRIMARY KEY (id)
) ENGINE = INNODB;

INSERT INTO family_group (name) VALUES ('PIXTON');
INSERT INTO family_group (name) VALUES ('LANDES');

INSERT INTO family (name, family_group_name) VALUES ('Mark and Robbi', 'PIXTON');
INSERT INTO family (name, family_group_name) VALUES ('Ben and Erika', 'PIXTON');
INSERT INTO family (name, family_group_name) VALUES ('Megan and Sam', 'PIXTON');
INSERT INTO family (name, family_group_name) VALUES ('Kim', 'PIXTON');
INSERT INTO family (name, family_group_name) VALUES ('Yeremy and Allison (Pixton)', 'PIXTON');
INSERT INTO family (name, family_group_name) VALUES ('Tom and Denise', 'LANDES');
INSERT INTO family (name, family_group_name) VALUES ('Michael and Rose', 'LANDES');
INSERT INTO family (name, family_group_name) VALUES ('Yeremy and Allison (Landes)', 'LANDES');
INSERT INTO family (name, family_group_name) VALUES ('Mike and Kellie', 'LANDES');

INSERT INTO person_type (type) VALUES ('PARENT');
INSERT INTO person_type (type) VALUES ('CHILD');

INSERT INTO person (name, type, family_name) VALUES ('Ben Pixton', 'PARENT', 'Ben and Erika');
INSERT INTO person (name, type, family_name) VALUES ('Erika Pixton', 'PARENT', 'Ben and Erika');
INSERT INTO person (name, type, family_name) VALUES ('Carly Pixton', 'CHILD', 'Ben and Erika');
INSERT INTO person (name, type, family_name) VALUES ('Colton Pixton', 'CHILD', 'Ben and Erika');

INSERT INTO person (name, type, family_name) VALUES ('Sam Christensen', 'PARENT', 'Megan and Sam');
INSERT INTO person (name, type, family_name) VALUES ('Megan Christensen', 'PARENT', 'Megan and Sam');
INSERT INTO person (name, type, family_name) VALUES ('Elara Christensen', 'CHILD', 'Megan and Sam');

INSERT INTO person (name, type, family_name) VALUES ('Kim Pixton', 'PARENT', 'Kim');
INSERT INTO person (name, type, family_name) VALUES ('Maddison Sigmon', 'CHILD', 'Kim');

INSERT INTO person (name, type, family_name) VALUES ('Yeremy Turcios', 'PARENT', 'Yeremy and Allison (Pixton)');
INSERT INTO person (name, type, family_name) VALUES ('Allison Turcios', 'PARENT', 'Yeremy and Allison (Pixton)');
INSERT INTO person (name, type, family_name) VALUES ('Ezekiel Turcios', 'CHILD', 'Yeremy and Allison (Pixton)');

INSERT INTO person (name, type, family_name) VALUES ('Yeremy Turcios', 'PARENT', 'Yeremy and Allison (Landes)');
INSERT INTO person (name, type, family_name) VALUES ('Allison Turcios', 'PARENT', 'Yeremy and Allison (Landes)');
INSERT INTO person (name, type, family_name) VALUES ('Ezekiel Turcios', 'CHILD', 'Yeremy and Allison (Landes)');

INSERT INTO person (name, type, family_name) VALUES ('Megan Landes', 'PARENT', 'Tom and Denise');

INSERT INTO person (name, type, family_name) VALUES ('Michael Landes', 'PARENT', 'Michael and Rose');
INSERT INTO person (name, type, family_name) VALUES ('Rose Landes', 'PARENT', 'Michael and Rose');
INSERT INTO person (name, type, family_name) VALUES ('Roman Landes', 'CHILD', 'Michael and Rose');
INSERT INTO person (name, type, family_name) VALUES ('Elikia Landes', 'CHILD', 'Michael and Rose');

INSERT INTO person (name, type, family_name) VALUES ('Mike Simpson', 'PARENT', 'Mike and Kellie');
INSERT INTO person (name, type, family_name) VALUES ('Kellie Simpson', 'PARENT', 'Mike and Kellie');

INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) VALUES (1, 'Ben Pixton', 10, 'Yeremy Turcios', 'PIXTON', 2015);
INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) VALUES (2, 'Erika Pixton', 11, 'Allison Turcios', 'PIXTON', 2015);
INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) VALUES (5, 'Sam Christensen', 1, 'Ben Pixton', 'PIXTON', 2015);
INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) VALUES (7, 'Megan Christensen', 3, 'Erika Pixton', 'PIXTON', 2015);
INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) VALUES (8, 'Kim Pixton', 5, 'Sam Christensen', 'PIXTON', 2015);
INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) VALUES (10, 'Yeremy Turcios', 6, 'Megan Christensen', 'PIXTON', 2015);
INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) VALUES (11, 'Allison Turcios', 8, 'Kim Pixton', 'PIXTON', 2015);
INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) VALUES (9, 'Maddison Sigmon', 3, 'Carly Pixton', 'PIXTON', 2015);
INSERT INTO exchange_history (giver_id, giver_name, receiver_id, receiver_name, family_group, year) VALUES (3, 'Carly Pixton', 9, 'Maddison Sigmon', 'PIXTON', 2015);