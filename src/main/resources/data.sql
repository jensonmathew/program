DROP TABLE IF EXISTS featureflags;  
CREATE TABLE featureflags (
id int(11) DEFAULT NULL, 
user varchar(15) DEFAULT NULL, 
email varchar(50) DEFAULT NULL, 
password varchar(15) DEFAULT NULL, 
active boolean DEFAULT NULL, 
roles varchar(15) DEFAULT NULL);
                        
INSERT INTO featureflags
(id, user, email, password, active, roles) 
VALUES
(101, 'testuser1','testuser1@t-mobile.com', 'password', true, 'user'),
(102, 'testuser2','testuser2@t-mobile.com', 'password', true, 'user'), 
(103, 'testuser3','testuser3@t-mobile.com', 'password', true, 'user'),
(104, 'testuser4','testuser4@t-mobile.com', 'password', true, 'user'),
(105, 'testuser5','testuser5@t-mobile.com', 'password', true, 'user'), 
(106, 'testuser6','testuser6@t-mobile.com', 'password', true, 'user'),
(107, 'testuser7','testuser7@t-mobile.com', 'password', true, 'user'),
(108, 'testuser8','testuser8@t-mobile.com', 'password', true, 'user'),
(109, 'testuser9','testuser9@t-mobile.com', 'password', true, 'user'),
(110, 'testuser10','testuser10@t-mobile.com', 'password', true, 'user');