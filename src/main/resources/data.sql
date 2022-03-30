DROP TABLE IF EXISTS featureflags;  
CREATE TABLE featureflags (
id INT(11) DEFAULT NULL, 
user VARCHAR2(15) DEFAULT NULL, 
email VARCHAR2(50) DEFAULT NULL, 
password VARCHAR2(15) DEFAULT NULL, 
active boolean DEFAULT NULL, 
roles VARCHAR2(15) DEFAULT NULL);
                        
INSERT INTO featureflags
(id, user, email, password, active, roles) 
VALUES
(1, 'testuser1','testuser1@t-mobile.com', 'password1', true, 'user'),
(2, 'testuser2','testuser2@t-mobile.com', 'password2', true, 'user'),
(3, 'testuser3','testuser3@t-mobile.com', 'password3', true, 'user'),
(4, 'testuser4','testuser4@t-mobile.com', 'password4', true, 'user'),
(5, 'testuser5','testuser5@t-mobile.com', 'password5', true, 'user'),
(6, 'testuser6','testuser6@t-mobile.com', 'password6', true, 'user'),
(7, 'testuser7','testuser7@t-mobile.com', 'password7', true, 'user'),
(8, 'testuser8','testuser8@t-mobile.com', 'password8', true, 'user'),
(9, 'testuser9','testuser9@t-mobile.com', 'password9', true, 'user'),
(0, 'testuser10','testuser10@t-mobile.com', 'password10', true, 'user');