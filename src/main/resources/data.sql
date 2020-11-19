
/*
INSERT INTO order_ (orderNumber) VALUES ('JDBC-ij03-oinw2');
INSERT INTO order_ (orderNumber) VALUES ('JDBC-vn35-iq40');
*/

INSERT INTO orders (order_number) VALUES ('JPA-ij03-oinw2');
INSERT INTO orders (order_number) VALUES ('JPA-vn35-iq40');

INSERT INTO USERS (username, password, enabled, first_name)
VALUES ('user', '$2a$10$9308j.z6YVE7oxPGo6JrYuG9xObN6IIjhl3hwTbhafTND/KWIVsBS', true, 'user');
INSERT INTO USERS (username, password, enabled, first_name)
VALUES ('admin', '$2a$10$mHMXKoFXcIL.cY1b6be0aeAVQRhEKj/uue.6c3kOekMq04CCJq1hW', true, 'admin');

INSERT INTO AUTHORITIES (username, authority)
VALUES ('user', 'USER');
INSERT INTO AUTHORITIES (username, authority)
VALUES ('admin', 'ADMIN');
INSERT INTO AUTHORITIES (username, authority)
VALUES ('admin', 'USER');