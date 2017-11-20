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

DROP TABLE IF EXISTS `user_log`;
CREATE TABLE `air_operation`.`user_log` (
  `log_id` varchar(20) NOT NULL,
  `user_id` VARCHAR(20) NOT NULL,
  `target` VARCHAR(45) NOT NULL,
  `message` LONGTEXT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`log_id`)
  );

##20171029
CREATE SCHEMA `air_measure` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `air_measure`.`machine` (
  `machine_id` VARCHAR(50) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`machine_id`));

CREATE TABLE `air_measure`.`qrcode` (
  `code_id` VARCHAR(20) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `code_status` TINYINT(1) NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`code_id`));

CREATE TABLE `air_measure`.`mq_bind` (
  `bind_id` VARCHAR(20) NOT NULL,
  `machine_id` VARCHAR(45) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `bind_status` TINYINT(1) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`bind_id`));

  
##2017.11.12
ALTER TABLE `airdevice`.`goods_model` 
ADD COLUMN `model_bonus` INT NOT NULL DEFAULT 0 AFTER `model_description`;


##2017.11.13
ALTER TABLE `airdevice`.`point_record` 
ADD COLUMN `order_id` VARCHAR(20) NULL AFTER `consumer_id`;


##2017.11.16
*#create system_log
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `air_operation`.`system_log` (
  `log_id` varchar(20) NOT NULL,
  `target` VARCHAR(45) NOT NULL,
  `message` LONGTEXT NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`log_id`)
  );

##2017.11.17
  # create VIEW
CREATE VIEW point_value_view
AS
SELECT
	point_record.record_id as record_id,
	point_record.order_id as order_id,
	point_record.consumer_id as consumer_id,
	goods_model.model_code as model_code,
	goods_model.model_bonus as model_bonus,
	point_record.create_time as create_time
from point_record, taobao_order, goods_model
WHERE point_record.order_id = taobao_order.order_id
			AND SUBSTRING(taobao_order.product_serial, 1, 3) = goods_model.model_code
			AND taobao_order.block_flag = 1

##2017.11.20
ALTER TABLE `goods_model`
ADD COLUMN `min_velocity`  int(11) NOT NULL AFTER `velocity`;