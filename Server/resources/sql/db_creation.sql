create table friendship (id_friendship int auto_increment primary key, first_user_id int not null, second_user_id int not null, confirmed tinyint(1) not null, creation_time datetime not null, confirmation_time datetime null) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
create table group1( id_group int auto_increment primary key, type enum('friendship', 'multiuser') not null, name int null, creation_time datetime not null) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
create table message( id_message int auto_increment primary key, from_id_user int not null, to_id_group int not null, send_time datetime not null, received_on_server_time datetime not null, deliver_time datetime not null, text mediumtext not null) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
create table user( id_user int auto_increment primary key, login varchar(20) not null, password char(64) not null, mail varchar(50) not null, name varchar(30) not null, surname varchar(30) not null, registration_time datetime not null, password_counter tinyint not null) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
create table user_group( id int auto_increment primary key, user_id int not null, group_id int not null, creation_time datetime not null) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
create procedure get_all_users()BEGIN SELECT id_user, login, mail, name, surname, registration_time, password_counter FROM user;END;
create procedure get_users_number()SELECT COUNT(*) as number FROM user;
create procedure login_guest(IN login1 varchar(20))BEGIN IF EXISTS(SELECT user.login FROM user WHERE user.login = login1) THEN SELECT 'login_not_available'; ELSE SELECT 'ok'; END IF;END;
create procedure login_user(IN login1 varchar(20), IN password1 varchar(30))BEGIN DECLARE result VARCHAR(20); DECLARE attempt_counter INT(1); IF EXISTS (SELECT user.login FROM user WHERE user.login = login1) THEN SET attempt_counter = (SELECT user.password_counter FROM user WHERE user.login = login1); IF (attempt_counter > 0) THEN IF SHA2(password1, 256) = (SELECT user.password FROM user WHERE user.login = login1) THEN UPDATE user SET password_counter = 3 WHERE user.login = login1; SET result = 'ok'; SELECT result, id_user, login, mail, name, surname, registration_time FROM user WHERE user.login = login1; ELSE SET result = 'wrong_password'; SET attempt_counter = attempt_counter-1; UPDATE user SET password_counter = attempt_counter WHERE user.login = login1; END IF; ELSE SET result = 'no_attempts'; END IF; ELSE SET result = 'no_user'; END IF; SELECT result, attempt_counter;END;
create procedure register_user(IN login varchar(20), IN password1 varchar(30), IN mail varchar(50), IN name varchar(30), IN surname varchar(30))BEGIN DECLARE result VARCHAR(20);IF EXISTS (SELECT user.login FROM user WHERE user.login = login) THEN SET result = 'login_not_available'; ELSE INSERT INTO user(login, password, mail, name, surname, registration_time, password_counter) VALUES(login, SHA2(password1, 256), mail, name, surname, NOW(), 3); SET result = 'ok';END IF;SELECT result;END;