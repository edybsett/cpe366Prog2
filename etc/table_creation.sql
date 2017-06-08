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
DROP TABLE ClassSession;
DROP TABLE ClassTimes;
DROP TABLE Class;

/**************************
 * Create Statements
 **************************/
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

/* Animals may be placed on hold for MAX_HOLD_DAYS.
   Up to 3 people can place a hold with priority 
   in order of who placed it. */
create table TempHold(
    animalId   INT REFERENCES Animal ON DELETE CASCADE, 
    customerId INT REFERENCES Login ON DELETE CASCADE,
    startDate  DATE,
    /* 1, 2, or 3 */
    priority   INT
);

/* Stores medical information about a specific animal.
   Conditions can be allergies, medical conditions, 
   or medical procedures. Bad name? 
   Each condition has an associated action to take
   for the animal. If they are allergic to chicken, the
   action is to not feed them any chicken. 
   These are not pre-determined. */
create table MedicalInfo(
    animalId    INT REFERENCES Animal ON DELETE CASCADE,
    name        TEXT,
    description TEXT,
    type        TEXT,
    action      TEXT
);

/* Class times. Pre-determined time slots available 
   for classes to be taught. */
create table ClassTimes(
    id        INT PRIMARY KEY,
    startTime TIME,
    endTime   TIME
);

/* Class has an id, name, and a primary teacher */
create table Class (
    id        SERIAL PRIMARY KEY,
    name      TEXT,
    teacherId INT REFERENCES Login ON DELETE CASCADE
);

/* A class session is a class with a teacher, which may
   be different from the primary teacher. It has an associated
   time interval and a price. */
create table ClassSession (
    id      SERIAL PRIMARY KEY,
    classId INT REFERENCES Class ON DELETE CASCADE,
    blockId INT,
    price   REAL check(price > 0),
    FOREIGN KEY (blockId) REFERENCES ClassTimes (id)
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
       ('bites'),
       ('barks'),
       ('trained');

/* Set time blocks */
INSERT INTO ClassTimes(id, startTime, endTime)
VALUES (101, '03:00 PM', '04:00 PM'),
       (102, '04:00 PM', '05:00 PM'),
       (103, '05:00 PM', '06:00 PM'),
       (104, '06:00 PM', '07:00 PM'),
       (201, '03:00 PM', '04:00 PM'),
       (202, '04:00 PM', '05:00 PM'),
       (203, '05:00 PM', '06:00 PM'),
       (204, '06:00 PM', '07:00 PM'),
       (301, '03:00 PM', '04:00 PM'),
       (302, '04:00 PM', '05:00 PM'),
       (303, '05:00 PM', '06:00 PM'),
       (304, '06:00 PM', '07:00 PM'),
       (401, '03:00 PM', '04:00 PM'),
       (402, '04:00 PM', '05:00 PM'),
       (403, '05:00 PM', '06:00 PM'),
       (404, '06:00 PM', '07:00 PM'),
       (501, '03:00 PM', '04:00 PM'),
       (502, '04:00 PM', '05:00 PM'),
       (503, '05:00 PM', '06:00 PM'),
       (504, '06:00 PM', '07:00 PM'),
       (601, '03:00 PM', '04:00 PM'),
       (602, '04:00 PM', '05:00 PM'),
       (603, '05:00 PM', '06:00 PM'),
       (604, '06:00 PM', '07:00 PM'),
       (701, '03:00 PM', '04:00 PM'),
       (702, '04:00 PM', '05:00 PM'),
       (703, '05:00 PM', '06:00 PM'),
       (704, '06:00 PM', '07:00 PM');

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