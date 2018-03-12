# Create Testuser
CREATE USER 'cryptofolio'@'localhost' IDENTIFIED BY 'ffDdU0oMjH1C9EIg';

GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON *.* TO 'cryptofolio'@'localhost';

# Create DB
CREATE DATABASE IF NOT EXISTS `cryptofolio` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `cryptofolio`;

# Create Table
CREATE TABLE IF NOT EXISTS `Coin`
(
  id int auto_increment
    primary key,
  externalId int null,
  url varchar(300) null,
  imageUrl varchar(300) null,
  shortName varchar(50) null,
  coinName varchar(100) null,
  constraint Coin_externalId_uindex
  unique (externalId),
  constraint Coin_shortName_uindex
  unique (shortName)
)
;

