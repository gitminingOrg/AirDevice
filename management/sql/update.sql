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
ALTER TABLE `airdevice`.`goods_model` 
ADD COLUMN `min_velocity` INT NOT NULL DEFAULT 0 AFTER `model_bonus`;

##2017.12.06
CREATE VIEW machine_status_view
AS
SELECT device_id, chip_id, model_code, model_name, update_time from device_chip, goods_model
WHERE SUBSTR(device_chip.device_id FROM 1 FOR 3) = goods_model.model_code
AND device_chip.`status` = 1
ORDER BY device_chip.update_time DESC

##2017.12.10
CREATE view latest_device_info as select device_id, pm25, power, time from deviceStatus where time >= subdate(now(), interval 20 minute) group by device_id;

CREATE TABLE `airdevice`.`install_insight` (
  `insight_id` VARCHAR(20) NOT NULL,
  `code_id` VARCHAR(45) NOT NULL,
  `insight_path` VARCHAR(200) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`insight_id`));

CREATE view device_address
as
SELECT device_id,
       `owner`,
       phone,
       city_name as city,
       province_name as province,
       address
       from deviceName, location_city, location_province
       WHERE deviceName.city_id = location_city.city_id
             AND
       deviceName.province_id = location_province.province_id

##2017.12.16
CREATE view qrcode_status_view
as
select code_id,
       code_value,
       qrcode.model_id,
       model_name,
       code_delivered,
       code_occupied,
       scan_time,
       qrcode.create_time
		from qrcode, goods_model
		WHERE qrcode.model_id = goods_model.model_id

DROP TABLE IF EXISTS `orderChannel`;
CREATE TABLE `airdevice`.`orderChannel` (
  `channel_id` VARCHAR (20) NOT NULL ,
  `channel_name` VARCHAR (50) NOT NULL ,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`channel_id`)
);

##2017年12月17日
ALTER TABLE `airdevice`.`install_insight`
ADD COLUMN `event_id` VARCHAR(20) NULL AFTER `code_id`;

INSERT INTO `airdevice`.`role` (`role_id`, `role_name`, `role_description`, `block_flag`, `create_time`) VALUES ('ROL00000005', 'order_viewer', '订单查看', '0', '2017-08-16 23:00:00');

##2017年12月20日
DROP VIEW IF EXISTS machine_status_view ;

CREATE VIEW machine_status_view
AS
SELECT device_id, chip_id, model_code, model_name, update_time from device_chip LEFT JOIN goods_model
on SUBSTR(device_chip.device_id FROM 1 FOR 3) = goods_model.model_code
AND device_chip.`status` = 1
GROUP BY device_chip.chip_id
ORDER BY device_chip.update_time DESC

##2017年12月24日
ALTER TABLE `taobao_order`
MODIFY COLUMN `buyer_ali_account`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `buyer_name`;

##2017年12月27日
CREATE TABLE `NewTable` (
`channel_id`  char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`channel_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`block_flag`  int(1) NOT NULL ,
`create_time`  datetime NOT NULL ,
PRIMARY KEY (`channel_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=COMPACT
;

##2017年12月29号
CREATE TABLE `NewTable` (
`channel_id`  char(11) NOT NULL ,
`channel_name`  varchar(255) NOT NULL ,
`block_flag`  int(1) NOT NULL ,
`create_time`  datetime NOT NULL ,
PRIMARY KEY (`channel_id`)
)
;



