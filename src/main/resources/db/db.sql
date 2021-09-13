--DB 생성
CREATE DATABASE board_project default CHARACTER SET UTF8;

--board 테이블 생성
CREATE TABLE board(
	postno INT Primary KEY AUTO_INCREMENT,
	subject VARCHAR(50) NOT NULL,
	content VARCHAR(4000),
	writer VARCHAR(32) NOT NULL,
	regdate DATE NOT NULL,
	modidate DATE NOT NULL,
	count INT DEFAULT 0
);

--board 테이블 필드 자료형 변경
ALTER TABLE board CHANGE regdate regdate datetime;
ALTER TABLE board CHANGE modidate modidate datetime;

--board_member 테이블 생성
CREATE TABLE `board_member` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(100) NOT NULL,
	`password` VARCHAR(100) NOT NULL,
	`name` VARCHAR(50) NOT NULL,
	`email` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `username` (`username`)
)

--board_reply 테이블 생성(댓글)
CREATE TABLE `board_reply` (
	`id_reply` INT(10) NOT NULL AUTO_INCREMENT,
	`content_reply` TEXT(65535) NOT NULL COLLATE 'utf8_general_ci',
	`postno` INT(11) NOT NULL,
	`id_member` BIGINT(20) NOT NULL,
	`reg_date` DATETIME NOT NULL,
	PRIMARY KEY (`id_reply`) USING BTREE,
	INDEX `FK__board` (`postno`) USING BTREE,
	INDEX `FK__board_member` (`id_member`) USING BTREE,
	CONSTRAINT `FK__board` FOREIGN KEY (`postno`) REFERENCES `board_project`.`board` (`postno`) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT `FK__board_member` FOREIGN KEY (`id_member`) REFERENCES `board_project`.`board_member` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

--board_member 테이블 role 필드 추가
ALTER TABLE board_member ADD role varcher(50)

--board_member 테이블 provider, providerId 필드 추가
ALTER TABLE `board_member`
	ADD COLUMN `provider` VARCHAR(100) NULL DEFAULT NULL AFTER `role`,
	ADD COLUMN `providerId` VARCHAR(100) NULL DEFAULT NULL AFTER `provider`;

--likes 테이블 생성
CREATE TABLE `likes` (
	`like_id` INT(11) NOT NULL AUTO_INCREMENT,
	`like_postno` INT(11) NOT NULL,
	`like_member` BIGINT(20) NOT NULL,
	`reg_date` DATETIME NOT NULL,
	`like_check` INT(11) NOT NULL,
	PRIMARY KEY (`like_id`) USING BTREE,
	INDEX `FK__board_like` (`like_postno`) USING BTREE,
	INDEX `FK__board_member_like` (`like_member`) USING BTREE,
	CONSTRAINT `FK__board_like` FOREIGN KEY (`like_postno`) REFERENCES `board_project`.`board` (`postno`) ON UPDATE RESTRICT ON DELETE CASCADE,
	CONSTRAINT `FK__board_member_like` FOREIGN KEY (`like_member`) REFERENCES `board_project`.`board_member` (`id`) ON UPDATE RESTRICT ON DELETE CASCADE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;