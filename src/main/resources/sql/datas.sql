SET NAMES utf8;

-- User datas

INSERT INTO `user` VALUES (1,'s.robert@localhost.fr','$2a$10$UFpm6lndzvle65G/HsnK9.IHuSk1vi443FoAOaV8gRNdb4jT72wZ.','Stéphane','Robert',97.41);
INSERT INTO `user` VALUES (2,'a.vaillancour@localhost.fr','$2a$10$ET/RfSeGA4/AqgII6UKgIeBTb4Y11nUMM9p08azkQu9VMtsVV7DTG','Alice','Vaillancour',6.98);
INSERT INTO `user` VALUES (3,'l.luzzi@localhost.fr','$2a$10$c4sMQsw23NHv5Uq9Wr5h5eD1W8pench1X2naAUFwvfWxWc8NZpWvy','Léo','Luzzi',0.44);

-- Connections datas

INSERT INTO `connection` VALUES (2,1);
INSERT INTO `connection` VALUES (3,1);
INSERT INTO `connection` VALUES (1,2);
INSERT INTO `connection` VALUES (1,3);


-- Transaction datas
INSERT INTO `transaction` VALUES (1,1,2,10.00,'Partage restaurant',0.05,'2022-09-03 10:42:12');
INSERT INTO `transaction` VALUES (2,1,3,8.00,'Place de cinéma',0.04,'2022-09-03 10:42:29');
INSERT INTO `transaction` VALUES (3,2,1,3.00,'Glace fête foraine',0.02,'2022-09-03 10:44:18');
INSERT INTO `transaction` VALUES (4,3,1,12.50,'Partage essence Lille > Paris',0.06,'2022-09-03 10:46:26');

-- Bank Account datas

INSERT INTO `bank_account` VALUES (1,1,'European Central Bank','EU120240360480600');
INSERT INTO `bank_account` VALUES (2,3,'Italian Bank','IT120240360480600720');



