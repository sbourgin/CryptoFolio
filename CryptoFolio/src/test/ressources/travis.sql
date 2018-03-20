# Create Testuser
CREATE USER 'cryptofolio'@'localhost' IDENTIFIED BY 'ffDdU0oMjH1C9EIg';

GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON *.* TO 'cryptofolio'@'localhost';

# Create DB
CREATE DATABASE IF NOT EXISTS `cryptofolio` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `cryptofolio`;

# Create Tables
CREATE TABLE IF NOT EXISTS `Coin`
(
  coin_id int auto_increment
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

CREATE TABLE IF NOT EXISTS `CoinPrice`
(
  coinprice_id int auto_increment
    primary key,
  coin_id int null,
  price decimal(15,5) null,
  date datetime null,
  constraint CoinPrice_Coin_id_fk
  foreign key (coin_id) references cryptofolio.Coin (coin_id)
)
;

create index coinId
  on CoinPrice (coin_id)
;
