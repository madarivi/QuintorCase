-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema musicdb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `musicdb` ;

-- -----------------------------------------------------
-- Schema musicdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `musicdb` DEFAULT CHARACTER SET utf8 ;
USE `musicdb` ;

-- -----------------------------------------------------
-- Table `musicdb`.`artists`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `musicdb`.`artists` ;

CREATE TABLE IF NOT EXISTS `musicdb`.`artists` (
  `artist_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `artist_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`artist_id`),
  UNIQUE INDEX `artist_id_UNIQUE` (`artist_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `musicdb`.`albums`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `musicdb`.`albums` ;

CREATE TABLE IF NOT EXISTS `musicdb`.`albums` (
  `album_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `artist_id` INT(10) UNSIGNED NOT NULL,
  `album_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`album_id`),
  UNIQUE INDEX `album_id_UNIQUE` (`album_id` ASC),
  INDEX `artist_id_idx` (`artist_id` ASC),
  CONSTRAINT `artist_id`
    FOREIGN KEY (`artist_id`)
    REFERENCES `musicdb`.`artists` (`artist_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `musicdb`.`songs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `musicdb`.`songs` ;

CREATE TABLE IF NOT EXISTS `musicdb`.`songs` (
  `song_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `album_id` INT(10) UNSIGNED NOT NULL,
  `song_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`song_id`),
  UNIQUE INDEX `song_id_UNIQUE` (`song_id` ASC),
  INDEX `album_id_idx` (`album_id` ASC),
  CONSTRAINT `album_id`
    FOREIGN KEY (`album_id`)
    REFERENCES `musicdb`.`albums` (`album_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
