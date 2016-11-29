SET MODE MySQL;

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