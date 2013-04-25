SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `japi-checker-web` ;
CREATE SCHEMA IF NOT EXISTS `japi-checker-web` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `japi-checker-web` ;

-- -----------------------------------------------------
-- Table `japi-checker-web`.`library`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`library` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`library` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`library_release`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`library_release` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`library_release` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `date` DATE NOT NULL ,
  `library_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_release_library_idx` (`library_id` ASC) ,
  CONSTRAINT `fk_release_library`
    FOREIGN KEY (`library_id` )
    REFERENCES `japi-checker-web`.`library` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`class`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`class` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`class` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(255) NOT NULL ,
  `access` INT NOT NULL ,
  `superName` VARCHAR(255) NOT NULL ,
  `version` INT NOT NULL ,
  `source` VARCHAR(255) NULL ,
  `release_id` INT NOT NULL ,
  `outer_class_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_class_release1_idx` (`release_id` ASC) ,
  INDEX `fk_class_class1_idx` (`outer_class_id` ASC) ,
  CONSTRAINT `fk_class_release1`
    FOREIGN KEY (`release_id` )
    REFERENCES `japi-checker-web`.`library_release` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_class_class1`
    FOREIGN KEY (`outer_class_id` )
    REFERENCES `japi-checker-web`.`class` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`implemented_interface`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`implemented_interface` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`implemented_interface` (
  `name` VARCHAR(255) NOT NULL ,
  `class_id` INT NOT NULL ,
  `list_index` INT NOT NULL ,
  PRIMARY KEY (`class_id`, `name`) ,
  INDEX `fk_implemented_interface_class1_idx` (`class_id` ASC) ,
  CONSTRAINT `fk_implemented_interface_class1`
    FOREIGN KEY (`class_id` )
    REFERENCES `japi-checker-web`.`class` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`field`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`field` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`field` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(255) NOT NULL ,
  `access` INT NOT NULL ,
  `descriptor` VARCHAR(255) NOT NULL ,
  `value` VARCHAR(255) NULL ,
  `class_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_field_class1_idx` (`class_id` ASC) ,
  CONSTRAINT `fk_field_class1`
    FOREIGN KEY (`class_id` )
    REFERENCES `japi-checker-web`.`class` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`method`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`method` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`method` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(255) NOT NULL ,
  `access` INT NOT NULL ,
  `descriptor` VARCHAR(1000) NOT NULL ,
  `value` VARCHAR(255) NULL ,
  `line` INT NULL ,
  `class_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_method_class1_idx` (`class_id` ASC) ,
  CONSTRAINT `fk_method_class1`
    FOREIGN KEY (`class_id` )
    REFERENCES `japi-checker-web`.`class` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`throwing_exception`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`throwing_exception` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`throwing_exception` (
  `name` VARCHAR(255) NOT NULL ,
  `method_id` INT NOT NULL ,
  `list_index` INT NOT NULL ,
  PRIMARY KEY (`name`, `method_id`) ,
  INDEX `fk_throwing_exception_method1_idx` (`method_id` ASC) ,
  CONSTRAINT `fk_throwing_exception_method1`
    FOREIGN KEY (`method_id` )
    REFERENCES `japi-checker-web`.`method` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`type_parameter`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`type_parameter` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`type_parameter` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `method_id` INT NULL ,
  `class_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_type_parameter_method1_idx` (`method_id` ASC) ,
  INDEX `fk_type_parameter_class1_idx` (`class_id` ASC) ,
  CONSTRAINT `fk_type_parameter_method1`
    FOREIGN KEY (`method_id` )
    REFERENCES `japi-checker-web`.`method` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_type_parameter_class1`
    FOREIGN KEY (`class_id` )
    REFERENCES `japi-checker-web`.`class` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`type_parameter_bound`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`type_parameter_bound` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`type_parameter_bound` (
  `name` VARCHAR(255) NOT NULL ,
  `type_parameter_id` INT NOT NULL ,
  `list_index` INT NOT NULL ,
  PRIMARY KEY (`name`, `type_parameter_id`) ,
  INDEX `fk_type_parameter_bound_type_parameter1_idx` (`type_parameter_id` ASC) ,
  CONSTRAINT `fk_type_parameter_bound_type_parameter1`
    FOREIGN KEY (`type_parameter_id` )
    REFERENCES `japi-checker-web`.`type_parameter` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`releases_comparison`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`releases_comparison` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`releases_comparison` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `reference_release_id` INT NOT NULL ,
  `new_release_id` INT NOT NULL ,
  `error_count` INT NOT NULL ,
  `warning_count` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_releases_comparison_release1_idx` (`reference_release_id` ASC) ,
  INDEX `fk_releases_comparison_release2_idx` (`new_release_id` ASC) ,
  CONSTRAINT `fk_releases_comparison_release1`
    FOREIGN KEY (`reference_release_id` )
    REFERENCES `japi-checker-web`.`library_release` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_releases_comparison_release2`
    FOREIGN KEY (`new_release_id` )
    REFERENCES `japi-checker-web`.`library_release` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`difference`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`difference` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`difference` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `reference_id` INT NOT NULL ,
  `new_id` INT NULL ,
  `difference_type` INT NOT NULL ,
  `element_type` VARCHAR(2) NOT NULL ,
  `new_element_type` VARCHAR(2) NULL ,
  `releases_comparison_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_difference_releases_comparison1_idx` (`releases_comparison_id` ASC) ,
  CONSTRAINT `fk_difference_releases_comparison1`
    FOREIGN KEY (`releases_comparison_id` )
    REFERENCES `japi-checker-web`.`releases_comparison` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `japi-checker-web`.`argument`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `japi-checker-web`.`argument` ;

CREATE  TABLE IF NOT EXISTS `japi-checker-web`.`argument` (
  `value` VARCHAR(255) NOT NULL ,
  `difference_id` INT NOT NULL ,
  `list_index` INT NOT NULL ,
  INDEX `fk_argument_difference1_idx` (`difference_id` ASC) ,
  PRIMARY KEY (`value`, `difference_id`) ,
  CONSTRAINT `fk_argument_difference1`
    FOREIGN KEY (`difference_id` )
    REFERENCES `japi-checker-web`.`difference` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `japi-checker-web` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
