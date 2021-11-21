DROP DATABASE IF EXISTS Assignment4;

CREATE DATABASE Assignment4;

USE Assignment4;


CREATE TABLE User(
UID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
Username VARCHAR(30) NOT NULL,
Password VARCHAR(15) NOT NULL,
Email VARCHAR(30) NOT NULL,
Balance DOUBLE NOT NULL,
AccountValue DOUBLE NOT NULL
);
CREATE TABLE Company(
CID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
Ticker VARCHAR(10) NOT NULL,
CompanyName VARCHAR(50),
ExchangeCode VARCHAR(10),
Price DOUBLE,
Change_ DOUBLE,
ChangePercentage DOUBLE,
StartDate VARCHAR(30),
Description_ VARCHAR(16000)
);

CREATE TABLE Stock(
SID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
CID INT NOT NULL,
	FOREIGN KEY fk2(CID) references Company(CID),
Prev_Close DOUBLE,
Mid_Price DOUBLE,
Ask_Price DOUBLE,
Ask_Size DOUBLE,
Bid_Price DOUBLE,
Bid_Size DOUBLE,
Open_ DOUBLE,
High_Price DOUBLE,
Low_Price DOUBLE,
Last DOUBLE,
Volume INT,
Date_Timestamp VARCHAR(50)
);


CREATE TABLE Favorites(
FID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
UID INT NOT NULL,
    FOREIGN KEY fk1(UID) references User(UID),
CID INT NOT NULL,
	FOREIGN KEY fk2(CID) references Company(CID)
);

CREATE TABLE Transactions(
TID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
UID INT NOT NULL,
    FOREIGN KEY fk1(UID) references User(UID),
CID INT NOT NULL,
	FOREIGN KEY fk2(CID) references Company(CID),
Quantity INT NOT NULL,
Cost DOUBLE NOT NULL
);

SELECT UID, Balance, CID
FROM User u, Company c
WHERE Username = 'jacubthomas' AND Ticker = '"AAPL"';

SELECT * FROM User;
SELECT * FROM Company;
SELECT * FROM Stock;
SELECT * FROM Favorites;
SELECT * FROM Transactions;

SELECT * FROM Transactions
WHERE UID = 2 AND CID = 6;

DROP TABLE Transactions;

UPDATE User SET AccountValue = 50000
WHERE UID = 2;


SELECT AccountValue FROM User WHERE UID = 2;

DELETE FROM Transactions
WHERE TID = 7;

UPDATE Transactions SET Quantity = 2
WHERE TID = 1;

INSERT INTO User(Username, Password, Email, Balance, AccountValue)
VALUES('test','123','test@gmail.com', 50000.00, 50000.00);


SELECT UID FROM User
WHERE Username = 'jacubthomas';

DELETE FROM User
WHERE Username = 'jacub';
