/* Table Creation File 
 * Includes:
 *   - DROP TABLE statements in correct order
 *   - CREATE TABLE statements
 *   - INSERT statements for initial data
 *   - GRANT statements to give everyone permission.
 */


/**************************
 * Drops 
 **************************/
DROP TABLE MedicalInfo;
DROP TABLE BreedXAnimal;
DROP TABLE Adoption;
DROP TABLE Personality;
DROP TABLE TempHold;
DROP TABLE MedicalCondition;
DROP TABLE Tag;
DROP TABLE Breed;
DROP TABLE Employee;
DROP TABLE Customer;
DROP TABLE Price;
DROP TABLE Animal;
DROP TABLE Login;
DROP TABLE Shelter;

/**************************
 * Create Statements
 **************************/

/* This is the animal shelter itself. There will
   only be one */
create table Shelter (
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    location    TEXT,
    dogCapacity INT,
    catCapacity INT
);

/* Login as well as personal information. A customer
   is only this information. Employee and Manager are
   different */
create table Login(
    id        SERIAL PRIMARY KEY,
    username  TEXT   UNIQUE,
    password  TEXT,
    firstName TEXT,
    lastName  TEXT,
    /* One of 'employee', 'customer', 'manager'*/
    role      TEXT
);

/* An animal has this base information */
create table Animal(
    id          SERIAL PRIMARY KEY,
    /* Age is usually a guess and this prevents us 
       from dealing with DATEs */
    ageYears     INT CHECK(ageYears < 100 AND ageYears > -1),
    ageMonths    INT CHECK(ageMonths < 12 AND ageMonths > -1),
    ageWeeks     INT CHECK(ageWeeks < 5 AND ageWeeks > -1),
    name         TEXT,
    weight       FLOAT,
    species      TEXT, /* 'cat' or 'dog' */
    description  TEXT,
    dateAdmitted DATE,
    color        TEXT, /* Can be anything */
    foodtype     TEXT, /* May be anything */
    sex          TEXT,
    energyLevel  INT,  /* 1 - 10 */
    image        BYTEA
);

/* Price is determined by age and species. This does
   not change */
create table Price(
    startAgeYear INT,
    endAgeYear   INT,
    species      TEXT,
    price        DECIMAL(6, 2)
);

/* This table is here in case we need it but we
   may never use it */
create table Customer(
    id    INT REFERENCES Login ON DELETE CASCADE PRIMARY KEY,
    email TEXT
);

/* Employees have a wage. Manager are employees. 
   Future information may be needed */
create table Employee(
    id   INT REFERENCES Login ON DELETE CASCADE,
    wage DECIMAL(6, 2)
);

/* Animal breeds. We aren't pre-specifing all breeds
   so we leave up to employees to enter correctly. 
   Animals may be mixed breed */
create table Breed(
    id   SERIAL PRIMARY KEY,
    type TEXT
);

/* Crosses Breed with Animal for that 
   many-to-many relationship */
create table BreedXAnimal(
    breedId  INT REFERENCES Breed ON DELETE CASCADE,
    animalId INT REFERENCES Animal ON DELETE CASCADE
);

/* Tags represent the animal personality. These
   will be pre-determined things like 'good with cats'
   or 'cuddly'. */
create table Tag(
    id          SERIAL PRIMARY KEY,
    description TEXT
);

/* Connects an animal to personality tags*/
create table Personality(
    animalId INT REFERENCES Animal ON DELETE CASCADE,
    tagId    INT REFERENCES Tag ON DELETE CASCADE
);

/* Just store who adopted whom */
create table Adoption(
    animalId   INT REFERENCES Animal ON DELETE CASCADE,
    customerId INT REFERENCES Customer ON DELETE CASCADE,
    day        DATE
);

/* Animals may be placed on hold for MAX_HOLD_DAYS.
   Up to 3 people can place a hold with priority 
   in order of who placed it. */
create table TempHold(
    animalId   INT REFERENCES Animal ON DELETE CASCADE, 
    customerId INT REFERENCES Customer ON DELETE CASCADE,
    startDate  DATE,
    /* 1, 2, or 3 */
    priority   INT
);

/* Conditions can be allergies, medical conditions, 
   or medical procedures. Bad name? 
   Each condition has an associated action to take
   for the animal. If they are allergic to chicken, the
   action is to not feed them any chicken. 
   These are not pre-determined. */
create table MedicalCondition(
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
    action      TEXT
);

/* Connects animals to conditions */
create table MedicalInfo(
    animalId INT REFERENCES Animal ON DELETE CASCADE,
    conditionId INT REFERENCES MedicalCondition ON DELETE CASCADE
);

/**************************
 * Initial, static data
 **************************/
/* One manager to start */
INSERT INTO Login(username, password, firstName, lastName, role)
VALUES ('admin', 'admin', 'Ad', 'Min', 'manager');
INSERT INTO Employee
VALUES ((select id from Login where username='admin'), 20.00);

/* Pricing as given by Wood's Humane Society except the timing 
   is a little different. */
INSERT INTO Price(startAgeYear, endAgeYear, species, price)
VALUES (0, 1, 'cat', 150.00),
       (0, 1, 'dog', 150.00),
       (1, 7, 'dog', 115.00),
       (1, 7, 'cat', 80.00),
       (7, 100, 'cat', 65.00),
       (7, 100, 'dog', 65.00);

/* Tags are pre-determined as the following */
/* May add more in the future */
INSERT INTO Tag(description)
VALUES ('good with dogs'),
       ('good with cats'),
       ('good with kids'),
       ('friendly'),
       ('bad with dogs'),
       ('bad with cats'),
       ('bad with kids'),
       ('shy'),
       ('cuddler'),
       ('spayed/neutered'),
       ('bites');

/**************************
 * Permissions for everyone 
 **************************/
GRANT ALL ON ALL TABLES IN SCHEMA public TO jkmar;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO jkmar;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public TO jkmar;
GRANT ALL ON ALL TABLES IN SCHEMA public TO mhetrick;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO mhetrick;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public TO mhetrick;
GRANT ALL ON ALL TABLES IN SCHEMA public TO edybsett;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO edybsett;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public TO edybsett;