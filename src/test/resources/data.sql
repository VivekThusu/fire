DROP TABLE IF EXISTS person;

CREATE TABLE person (
  first_name VARCHAR(250) PRIMARY KEY,
  last_name VARCHAR(250) NOT NULL,
  age INT NOT NULL,
  favourite_colour VARCHAR(250)
);

INSERT INTO person (first_name, last_name, age, favourite_colour) VALUES
  ('JohnTest', 'Doe', 66, 'red'),
  ('BillTest', 'Gates', 50, 'blue'),
  ('JeffTest', 'Hardy', 36, 'peach');