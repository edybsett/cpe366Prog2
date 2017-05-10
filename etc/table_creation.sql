/* Table Creation Statements */

create table Shelter (
	id          SERIAL PRIMARY KEY,
	name        TEXT,
	location    TEXT,
	dogCapacity INT,
	catCapacity INT
);

create table Login(
	id        SERIAL PRIMARY KEY,
	username  TEXT   UNIQUE,
	password  TEXT,
	firstName TEXT,
	lastName  TEXT
);

create table Animal(
	id        SERIAL PRIMARY KEY,
	ageYears  INT,
	ageMonths INT,
	name      TEXT,
	weight    FLOAT,
	type      TEXT,
        description TEXT,
        image BLOB
);

create table Price(
	startAgeYear  INT,
	startAgeMonth INT,
	endAgeYear    INT,
	endAgeMonth   INT,
	price         DECIMAL(3, 2)
);

create table Customer(
	id INT REFERENCES Login(id) PRIMARY KEY
);

create table Employee(
	id   INT REFERENCES Login(id) PRIMARY KEY,
	wage DECIMAL(3, 2)	
);

create table Breed(
	id   SERIAL PRIMARY KEY,
	type TEXT
);

create table BreedXAnimal(
	breedId  INT REFERENCES Breed(id),
	animalId INT REFERENCES Animal(id)
);

create table Personality(
	animalId    INT REFERENCES Animal(id) PRIMARY KEY,
	energyLevel INT,
	goodwcats   BOOLEAN,
	goodwdogs   BOOLEAN,
	goodwkids   BOOLEAN,
	shy         BOOLEAN,
	description TEXT
);

create table Adoptions(
	animalId   INT REFERENCES Animal(id),
	customerId INT REFERENCES Customer(id),
	day        DATE
);


create table MedicalInfo(
	animalId INT REFERENCES Animal(id) PRIMARY KEY,
	foodType TEXT
);

create table Condition(
	id SERIAL PRIMARY KEY,
	name TEXT,
	/* One of allergy, condition, procedure */
	description TEXT 
);

create table ConditionXMedical(
	medicalId   INT REFERENCES MedicalInfo(animalId),
	conditionId INT REFERENCES Condition(id)
);
