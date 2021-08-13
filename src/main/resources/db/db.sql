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
	`username` VARCHAR(50) NOT NULL,
	`password` VARCHAR(100) NOT NULL,
	`name` VARCHAR(50) NOT NULL,
	`email` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `username` (`username`)
)
