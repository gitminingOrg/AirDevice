DROP TABLE IF EXISTS `pre_binding`;
CREATE TABLE `pre_binding` (
  `bind_id` varchar(20) NOT NULL AUTO_INCREMENT,
  `uid` char(255) NOT NULL,
  `code_id` varchar(255) NOT NULL,
  `block_flag` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`bind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `airdevice`.`idle_machine` (
  `im_id` VARCHAR(20) NOT NULL,
  `uid` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`im_id`));

CREATE SCHEMA `air_operation` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `air_operation`.`user_log` (
  `log_id` VARCHAR(20) NOT NULL,
  `user_id` VARCHAR(20) NOT NULL,
  `target` VARCHAR(45) NOT NULL,
  `message` LONGTEXT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`log_id`));
