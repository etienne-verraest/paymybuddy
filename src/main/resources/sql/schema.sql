-- USER
CREATE TABLE user (
  id int unsigned NOT NULL AUTO_INCREMENT,
  mail varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  balance decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (id),
  UNIQUE KEY mail (mail)
) DEFAULT CHARSET=utf8;

-- BANK_ACCOUNT
CREATE TABLE bank_account (
  id int unsigned NOT NULL AUTO_INCREMENT,
  user_id int unsigned NOT NULL,
  bank_name varchar(100) DEFAULT NULL,
  iban varchar(100) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY fk_id_bank (user_id),
  CONSTRAINT fk_id_bank FOREIGN KEY (user_id) REFERENCES user (id)
) DEFAULT CHARSET=utf8;

-- CONNECTION
CREATE TABLE connection (
  id_user int unsigned NOT NULL,
  id_buddy int unsigned NOT NULL,
  PRIMARY KEY (id_user,id_buddy),
  KEY fk_connection_buddy (id_buddy),
  CONSTRAINT fk_connection_user FOREIGN KEY (id_user) REFERENCES user (id),
  CONSTRAINT fk_connection_buddy FOREIGN KEY (id_buddy) REFERENCES user (id)
) DEFAULT CHARSET=utf8;

-- TRANSACTION
CREATE TABLE transaction (
  id int unsigned NOT NULL AUTO_INCREMENT,
  sender_id int unsigned NOT NULL,
  recipient_id int unsigned NOT NULL,
  amount decimal(10,2) NOT NULL,
  description varchar(150) DEFAULT NULL,
  fee decimal(10,2) NOT NULL,
  date datetime DEFAULT NULL,
  PRIMARY KEY (id),
  KEY fk_sender_ref_user (sender_id),
  KEY fk_recipient_ref_user (recipient_id),
  CONSTRAINT fk_recipient_ref_user FOREIGN KEY (recipient_id) REFERENCES user (id),
  CONSTRAINT fk_sender_ref_user FOREIGN KEY (sender_id) REFERENCES user (id)
) DEFAULT CHARSET=utf8;