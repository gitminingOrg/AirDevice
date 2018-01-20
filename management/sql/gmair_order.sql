-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_order
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gmair_order
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_order` DEFAULT CHARACTER SET utf8 ;
USE `gmair_order` ;

-- -----------------------------------------------------
-- Table `gmair_order`.`order_channel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`order_channel` (
  `channel_id` VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 1,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`channel_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_order`.`order_diversion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`order_diversion` (
  `diversion_id` VARCHAR(20) NOT NULL,
  `diversion_name` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 1,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`diversion_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_order`.`guomai_order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`guomai_order` (
  `order_id` VARCHAR(20) NOT NULL,
  `order_no` VARCHAR(45) NULL,
  `buyer_name` VARCHAR(45) NULL,
  `receiver_name` VARCHAR(45) NULL,
  `receiver_phone` VARCHAR(45) NULL,
  `receiver_province` VARCHAR(45) NULL,
  `receiver_city` VARCHAR(45) NULL,
  `receiver_district` VARCHAR(45) NULL,
  `receiver_address` VARCHAR(150) NULL,
  `coupon_no` VARCHAR(45) NULL,
  `diversion_id` VARCHAR(20) NULL,
  `channel_id` VARCHAR(20) NULL,
  `order_status` TINYINT(1) NOT NULL DEFAULT 1,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_guomai_order_order_channel1_idx` (`channel_id` ASC),
  INDEX `fk_guomai_order_order_diversion1_idx` (`diversion_id` ASC),
  CONSTRAINT `fk_guomai_order_order_channel1`
    FOREIGN KEY (`channel_id`)
    REFERENCES `gmair_order`.`order_channel` (`channel_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_guomai_order_order_diversion1`
    FOREIGN KEY (`diversion_id`)
    REFERENCES `gmair_order`.`order_diversion` (`diversion_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_order`.`order_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`order_item` (
  `order_item_id` VARCHAR(20) NOT NULL,
  `order_id` VARCHAR(20) NOT NULL,
  `com_id` VARCHAR(20) NULL,
  `order_item_quantity` INT NOT NULL DEFAULT 1,
  `order_item_status` TINYINT(1) NOT NULL DEFAULT 1,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`order_item_id`),
  INDEX `fk_order_item_guomai_order_idx` (`order_id` ASC),
  CONSTRAINT `fk_order_item_guomai_order`
    FOREIGN KEY (`order_id`)
    REFERENCES `gmair_order`.`guomai_order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_order`.`setup_provider`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`setup_provider` (
  `provider_id` VARCHAR(20) NOT NULL COMMENT 'Randomly generated serial',
  `provider_name` VARCHAR(45) NULL COMMENT 'Set up provider name',
  `block_flag` TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'Current status of the record',
  `create_time` DATETIME NOT NULL COMMENT 'Record create time',
  PRIMARY KEY (`provider_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_order`.`machine_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`machine_item` (
  `machine_item_id` VARCHAR(20) NOT NULL,
  `order_item_id` VARCHAR(20) NOT NULL,
  `provider_id` VARCHAR(20) NOT NULL,
  `machine_code` VARCHAR(45) NULL,
  `machine_status` TINYINT(1) NOT NULL DEFAULT 1,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 1,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`machine_item_id`),
  INDEX `fk_machine_item_order_item1_idx` (`order_item_id` ASC),
  INDEX `fk_machine_item_setup_provider1_idx` (`provider_id` ASC),
  CONSTRAINT `fk_machine_item_order_item1`
    FOREIGN KEY (`order_item_id`)
    REFERENCES `gmair_order`.`order_item` (`order_item_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_machine_item_setup_provider1`
    FOREIGN KEY (`provider_id`)
    REFERENCES `gmair_order`.`setup_provider` (`provider_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_order`.`machine_mission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`machine_mission` (
  `mission_id` VARCHAR(20) NOT NULL,
  `machine_item_id` VARCHAR(20) NOT NULL,
  `mission_title` VARCHAR(45) NOT NULL,
  `mission_content` VARCHAR(200) NULL,
  `mission_recorder` VARCHAR(45) NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`mission_id`),
  INDEX `fk_machine_mission_machine_item1_idx` (`machine_item_id` ASC),
  CONSTRAINT `fk_machine_mission_machine_item1`
    FOREIGN KEY (`machine_item_id`)
    REFERENCES `gmair_order`.`machine_item` (`machine_item_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gmair_order`.`order_commodity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_order`.`order_commodity` (
  `com_id` VARCHAR(20) NOT NULL,
  `com_name` VARCHAR(45) NULL,
  `com_type` TINYINT(1) NOT NULL DEFAULT 0,
  `com_price` DOUBLE NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`com_id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
