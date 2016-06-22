--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'SQL_ASCII';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "user" (
    id integer NOT NULL,
    person_id integer,
    username text NOT NULL,
    password text NOT NULL,
    rating real NOT NULL,
    created timestamp with time zone NOT NULL,
    updated timestamp with time zone NOT NULL,
    CONSTRAINT "MyRatingCheck" CHECK (((rating >= (0)::double precision) AND (rating <= (5)::double precision)))
);


ALTER TABLE "user" OWNER TO postgres;

--
-- Name: USER_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "USER_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "USER_id_seq" OWNER TO postgres;

--
-- Name: USER_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "USER_id_seq" OWNED BY "user".id;


--
-- Name: address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE address (
    id integer NOT NULL,
    line1 text NOT NULL,
    line2 text,
    city text NOT NULL,
    state_id integer NOT NULL,
    zip text NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE address OWNER TO postgres;

--
-- Name: address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE address_id_seq OWNER TO postgres;

--
-- Name: address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE address_id_seq OWNED BY address.id;


--
-- Name: bid; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE bid (
    id integer NOT NULL,
    item_id integer NOT NULL,
    user_id integer NOT NULL,
    amount numeric NOT NULL,
    created timestamp with time zone NOT NULL,
    payment_id integer NOT NULL,
    CONSTRAINT bid_amount_check CHECK ((amount > (0)::numeric))
);


ALTER TABLE bid OWNER TO postgres;

--
-- Name: bid_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE bid_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bid_id_seq OWNER TO postgres;

--
-- Name: bid_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE bid_id_seq OWNED BY bid.id;


--
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE categories (
    id integer NOT NULL,
    item_id integer NOT NULL,
    category_id integer NOT NULL
);


ALTER TABLE categories OWNER TO postgres;

--
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE categories_id_seq OWNER TO postgres;

--
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE categories_id_seq OWNED BY categories.id;


--
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE category (
    id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE category OWNER TO postgres;

--
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE category_id_seq OWNER TO postgres;

--
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE category_id_seq OWNED BY category.id;


--
-- Name: feedback; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE feedback (
    id integer NOT NULL,
    message text,
    created timestamp with time zone NOT NULL,
    updated timestamp with time zone NOT NULL,
    rating integer NOT NULL,
    CONSTRAINT feedback_rating_check CHECK (((rating >= 0) AND (rating <= 5)))
);


ALTER TABLE feedback OWNER TO postgres;

--
-- Name: feedback_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE feedback_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE feedback_id_seq OWNER TO postgres;

--
-- Name: feedback_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE feedback_id_seq OWNED BY feedback.id;


--
-- Name: item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE item (
    id integer NOT NULL,
    name text NOT NULL,
    description text,
    price numeric NOT NULL,
    start_date timestamp with time zone NOT NULL,
    end_date timestamp with time zone NOT NULL,
    image text,
    buyer_feedback_id integer NOT NULL,
    seller_feedback_id integer NOT NULL,
    winning_bid_id integer,
    user_id integer NOT NULL,
    created timestamp with time zone NOT NULL,
    updated timestamp with time zone NOT NULL,
    CONSTRAINT item_price_check CHECK ((price > (0)::numeric))
);


ALTER TABLE item OWNER TO postgres;

--
-- Name: item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE item_id_seq OWNER TO postgres;

--
-- Name: item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE item_id_seq OWNED BY item.id;


--
-- Name: payment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE payment (
    id integer NOT NULL,
    person_id integer NOT NULL,
    number text NOT NULL,
    expiration_date date NOT NULL,
    address_id integer NOT NULL,
    csv integer NOT NULL
);


ALTER TABLE payment OWNER TO postgres;

--
-- Name: payment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE payment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE payment_id_seq OWNER TO postgres;

--
-- Name: payment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE payment_id_seq OWNED BY payment.id;


--
-- Name: person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE person (
    id integer NOT NULL,
    first text NOT NULL,
    middle text,
    last text NOT NULL,
    birthday date
);


ALTER TABLE person OWNER TO postgres;

--
-- Name: person_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE person_id_seq OWNER TO postgres;

--
-- Name: person_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE person_id_seq OWNED BY person.id;


--
-- Name: state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE state (
    id integer NOT NULL,
    name text NOT NULL,
    code character(2) NOT NULL
);


ALTER TABLE state OWNER TO postgres;

--
-- Name: state_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE state_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE state_id_seq OWNER TO postgres;

--
-- Name: state_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE state_id_seq OWNED BY state.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address ALTER COLUMN id SET DEFAULT nextval('address_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bid ALTER COLUMN id SET DEFAULT nextval('bid_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categories ALTER COLUMN id SET DEFAULT nextval('categories_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY category ALTER COLUMN id SET DEFAULT nextval('category_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY feedback ALTER COLUMN id SET DEFAULT nextval('feedback_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item ALTER COLUMN id SET DEFAULT nextval('item_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY payment ALTER COLUMN id SET DEFAULT nextval('payment_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person ALTER COLUMN id SET DEFAULT nextval('person_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY state ALTER COLUMN id SET DEFAULT nextval('state_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('"USER_id_seq"'::regclass);


--
-- Name: USER_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"USER_id_seq"', 9, true);


--
-- Data for Name: address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY address (id, line1, line2, city, state_id, zip, user_id) FROM stdin;
1	12345 Joe St.	\N	Seattle	52	98000	1
\.


--
-- Name: address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('address_id_seq', 1, true);


--
-- Data for Name: bid; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY bid (id, item_id, user_id, amount, created, payment_id) FROM stdin;
1	1	1	1000	2016-06-21 23:40:16.424-07	3
\.


--
-- Name: bid_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('bid_id_seq', 1, true);


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY categories (id, item_id, category_id) FROM stdin;
1	1	1
\.


--
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('categories_id_seq', 1, true);


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY category (id, name) FROM stdin;
1	accessories 
\.


--
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('category_id_seq', 1, true);


--
-- Data for Name: feedback; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY feedback (id, message, created, updated, rating) FROM stdin;
2	blabla	2016-06-21 23:18:55.202-07	2016-06-21 23:18:56.197-07	5
\.


--
-- Name: feedback_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('feedback_id_seq', 2, true);


--
-- Data for Name: item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY item (id, name, description, price, start_date, end_date, image, buyer_feedback_id, seller_feedback_id, winning_bid_id, user_id, created, updated) FROM stdin;
1	bag	nice bag	99.99	2016-06-21 23:36:12.735-07	2016-07-21 23:36:14.495-07	\N	2	2	1	1	2016-06-21 23:39:26.854-07	2016-06-21 23:39:28.459-07
\.


--
-- Name: item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('item_id_seq', 1, true);


--
-- Data for Name: payment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY payment (id, person_id, number, expiration_date, address_id, csv) FROM stdin;
3	1	123123123123	2016-06-21	1	999
\.


--
-- Name: payment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('payment_id_seq', 3, true);


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY person (id, first, middle, last, birthday) FROM stdin;
1	John	Jameson	Smith	2016-06-21
2	Janet	Rose	Doe	2016-06-05
6	Rachel	Apple	Cinnamon	2016-06-08
7	Homer	RRR	Simpson	2016-05-04
\.


--
-- Name: person_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('person_id_seq', 7, true);


--
-- Data for Name: state; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY state (id, name, code) FROM stdin;
1	Alabama	AL
2	Alaska	AK
3	Arizona	AZ
4	Arkansas	AR
5	California	CA
6	Colorado	CO
7	Connecticut	CT
8	Delaware	DE
9	District of Columbia	DC
10	Florida	FL
11	Georgia	GA
12	Hawaii	HI
13	Idaho	ID
14	Illinois	IL
15	Indiana	IN
16	Iowa	IA
17	Kansas	KS
18	Kentucky	KY
19	Louisiana	LA
20	Maine	ME
21	Maryland	MD
22	Massachusetts	MA
23	Michigan	MI
24	Minnesota	MN
25	Mississippi	MS
26	Missouri	MO
27	Montana	MT
28	Nebraska	NE
29	Nevada	NV
30	New Hampshire	NH
31	New Jersey	NJ
32	New Mexico	NM
33	New York	NY
34	North Carolina	NC
35	North Dakota	ND
36	Ohio	OH
37	Oklahoma	OK
38	Oregon	OR
39	Pennsylvania	PA
40	Puerto Rico	PR
41	Rhode Island	RI
42	South Carolina	SC
43	South Dakota	SD
44	Tennessee	TN
45	Texas	TX
46	Utah	UT
47	Vermont	VT
48	Virginia	VA
49	Washington	WA
50	West Virginia	WV
51	Wisconsin	WI
52	Wyoming	WY
\.


--
-- Name: state_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('state_id_seq', 52, true);


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY "user" (id, person_id, username, password, rating, created, updated) FROM stdin;
1	2	lala	qwerty	5	2016-06-21 21:03:03.313-07	2016-06-21 21:03:06.105-07
3	1	bubu	12345	2	2016-06-21 21:54:57.044-07	2016-06-21 21:55:00.16-07
5	6	ruru	god	4	2016-06-21 22:02:05.032-07	2016-06-21 22:02:08.499-07
9	2	lolo	password	5	2016-06-21 22:17:22.656-07	2016-06-21 22:17:23.814-07
\.


--
-- Name: address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);


--
-- Name: bid_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bid
    ADD CONSTRAINT bid_pkey PRIMARY KEY (id);


--
-- Name: categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- Name: category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: feedback_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY feedback
    ADD CONSTRAINT feedback_pkey PRIMARY KEY (id);


--
-- Name: item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);


--
-- Name: payment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);


--
-- Name: person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- Name: state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY state
    ADD CONSTRAINT state_pkey PRIMARY KEY (id);


--
-- Name: user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: address_state_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_state_id_fkey FOREIGN KEY (state_id) REFERENCES state(id);


--
-- Name: address_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: bid_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bid
    ADD CONSTRAINT bid_item_id_fkey FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: bid_payment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bid
    ADD CONSTRAINT bid_payment_id_fkey FOREIGN KEY (payment_id) REFERENCES payment(id);


--
-- Name: bid_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bid
    ADD CONSTRAINT bid_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: categories_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categories
    ADD CONSTRAINT categories_category_id_fkey FOREIGN KEY (category_id) REFERENCES category(id);


--
-- Name: categories_category_id_fkey1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categories
    ADD CONSTRAINT categories_category_id_fkey1 FOREIGN KEY (category_id) REFERENCES item(id);


--
-- Name: categories_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categories
    ADD CONSTRAINT categories_item_id_fkey FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: item_buyer_feedback_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_buyer_feedback_id_fkey FOREIGN KEY (buyer_feedback_id) REFERENCES feedback(id);


--
-- Name: item_seller_feedback_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_seller_feedback_id_fkey FOREIGN KEY (seller_feedback_id) REFERENCES feedback(id);


--
-- Name: item_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: payment_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_address_id_fkey FOREIGN KEY (address_id) REFERENCES address(id);


--
-- Name: payment_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY payment
    ADD CONSTRAINT payment_person_id_fkey FOREIGN KEY (person_id) REFERENCES person(id);


--
-- Name: user_person_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_person_id_fk FOREIGN KEY (person_id) REFERENCES person(id);


--
-- Name: winning_bid_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item
    ADD CONSTRAINT winning_bid_fk FOREIGN KEY (winning_bid_id) REFERENCES bid(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

