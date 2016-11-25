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

INSERT INTO person (name, type, family_name) VALUES ('Sam Pixton', 'PARENT', 'Mark and Robbi');

INSERT INTO person (name, type, family_name) VALUES ('Ben Pixton', 'PARENT', 'Ben and Erika');
INSERT INTO person (name, type, family_name) VALUES ('Erika Pixton', 'PARENT', 'Ben and Erika');
INSERT INTO person (name, type, family_name) VALUES ('Carly Pixton', 'CHILD', 'Ben and Erika');
INSERT INTO person (name, type, family_name) VALUES ('Colton Pixton', 'CHILD', 'Ben and Erika');

INSERT INTO person (name, type, family_name) VALUES ('Sam Christensen', 'PARENT', 'Megan and Sam');
INSERT INTO person (name, type, family_name) VALUES ('Megan Christensen', 'PARENT', 'Megan and Sam');
INSERT INTO person (name, type, family_name) VALUES ('Elara Christensen', 'CHILD', 'Megan and Sam');

INSERT INTO person (name, type, family_name) VALUES ('Kim Pixton', 'PARENT', 'Kim');
INSERT INTO person (name, type, family_name) VALUES ('Madison', 'CHILD', 'Kim');

INSERT INTO person (name, type, family_name) VALUES ('Yeremy Turcios', 'PARENT', 'Yeremy and Allison (Pixton)');
INSERT INTO person (name, type, family_name) VALUES ('Allison Turcios', 'PARENT', 'Yeremy and Allison (Pixton)');
INSERT INTO person (name, type, family_name) VALUES ('Ezekiel', 'CHILD', 'Yeremy and Allison (Pixton)');

INSERT INTO person (name, type, family_name) VALUES ('Yeremy Turcios', 'PARENT', 'Yeremy and Allison (Landes)');
INSERT INTO person (name, type, family_name) VALUES ('Allison Turcios', 'PARENT', 'Yeremy and Allison (Landes)');
INSERT INTO person (name, type, family_name) VALUES ('Ezekiel', 'CHILD', 'Yeremy and Allison (Landes)');

INSERT INTO person (name, type, family_name) VALUES ('Megan Landes', 'PARENT', 'Tom and Denise');

INSERT INTO person (name, type, family_name) VALUES ('Michael Landes', 'PARENT', 'Michael and Rose');
INSERT INTO person (name, type, family_name) VALUES ('Rose Landes', 'PARENT', 'Michael and Rose');
INSERT INTO person (name, type, family_name) VALUES ('Roman Landes', 'CHILD', 'Michael and Rose');
INSERT INTO person (name, type, family_name) VALUES ('Elikia Landes', 'CHILD', 'Michael and Rose');

INSERT INTO person (name, type, family_name) VALUES ('Mike Simpson', 'PARENT', 'Mike and Kellie');
INSERT INTO person (name, type, family_name) VALUES ('Kellie Simpson', 'PARENT', 'Mike and Kellie');
