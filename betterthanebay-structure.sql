CREATE TABLE PERSON(
	ID INT PRIMARY KEY NOT NULL,
	FIRST TEXT NOT NULL,
	MIDDLE TEXT,
	LAST TEXT NOT NULL,
	BIRTHDAY DATE
);

CREATE TABLE USER(
	ID INT PRIMARY KEY NOT NULL,
	PERSON_ID INT REFERENCES PERSON(ID),
	USERNAME TEXT NOT NULL,
	PASSWORD TEXT NOT NULL,
	RATING REAL NOT NULL CHECK (RATING >= 0 AND RATING <= 5),
	CREATED TIMESTAMPTZ NOT NULL,
	UPDATED TIMESTAMPTZ NOT NULL
);

CREATE TABLE STATE(
	ID INT PRIMARY KEY NOT NULL,
	NAME TEXT NOT NULL,
	CODE CHAR(2) NOT NULL
);

CREATE TABLE ADDRESS(
	ID INT PRIMARY KEY NOT NULL,
	LINE1 TEXT NOT NULL,
	LINE2 TEXT,
	CITY TEXT NOT NULL,
	STATE_ID INT REFERENCES STATE(ID)
	ZIP TEXT NOT NULL,
	USER_ID INT REFERENCES USER(ID)
);

CREATE TABLE PAYMENT(
	ID INT PRIMARY KEY NOT NULL,
	PERSON_ID INT REFERENCES PERSON(ID),
	NUMBER TEXT NOT NULL,
	EXPIRY DATE NOT NULL,
	ADDRESS_ID INT REFERENCES ADDRESS(ID),
	CSV INT
);

CREATE TABLE FEEDBACK(
	ID INT PRIMARY KEY NOT NULL,
	MESSAGE TEXT,
	CREATED TIMESTAMPTZ NOT NULL,
	UPDATED TIMESTAMPTZ NOT NULL,
	RATING INT NOT NULL CHECK (RATING >= 0 AND RATING <= 5)
);

CREATE TABLE ITEM(
	ID INT PRIMARY KEY NOT NULL,
	NAME TEXT NOT NULL,
	DESCRIPTION TEXT,
	PRICE REAL CHECK (PRICE > 0),
	START_DATE TIMESTAMPTZ NOT NULL,
	END_DATE TIMESTAMPTZ NOT NULL,
	IMAGE TEXT,
	BUYER_FEEDBACK_ID INT REFERENCES FEEDBACK(ID),
	SELLER_FEEDBACK_ID INT REFERENCES FEEDBACK(ID),
	WINNING_BID_ID INT REFERENCES BID(ID),
	USER_ID INT REFERENCES USER(ID),
	CREATED TIMESTAMPTZ NOT NULL,
	UPDATED TIMESTAMPTZ NOT NULL
);

CREATE TABLE CATEGORY(
	ID INT PRIMARY KEY NOT NULL,
	NAME TEXT NOT NULL
);

CREATE TABLE CATEGORIES(
	ID INT PRIMARY KEY NOT NULL,
	ITEM_ID INT REFERENCES ITEM(ID),
	CATEGORY_ID INT REFERENCES CATEGORY(ID)
);

CREATE TABLE BID(
	ID INT PRIMARY KEY NOT NULL,
	ITEM_ID INT REFERENCES ITEM(ID),
	USER_ID INT REFERENCES USER(ID),
	AMOUNT REAL CHECK (AMOUNT > 0),
	CREATED TIMESTAMPTZ NOT NULL,
	PAYMENT_ID INT REFERENCES PAYMENT(ID)
);