-- Adminer 4.3.0 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP DATABASE IF EXISTS `library_db`;
CREATE DATABASE `library_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `library_db`;

DROP TABLE IF EXISTS `booked_documents`;
CREATE TABLE `booked_documents` (
  `user_id` int(11) NOT NULL,
  `document_id` int(11) NOT NULL,
  `return_till` int(11) NOT NULL,
  `renewed` int(11) NOT NULL DEFAULT '0',
  `asked_to_return` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `booked_documents` (`user_id`, `document_id`, `return_till`, `renewed`, `asked_to_return`) VALUES
(6, 31, 1523007531, 1,  1),
(6, 29, 1522926591, 1,  1),
(6, 28, 1524666311, 0,  0),
(6, 32, 1525271012, 1,  0),
(1, 8,  1529271232, 1,  0);

DROP TABLE IF EXISTS `documents`;
CREATE TABLE `documents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `authors` text NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `additional_info` text NOT NULL,
  `keywords` text NOT NULL,
  `instock_count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

INSERT INTO `documents` (`id`, `type`, `title`, `authors`, `price`, `additional_info`, `keywords`, `instock_count`) VALUES
(8, 0,  'Test Book 8',  'Ignatov Valery N., ...', 123.45, '{\"reference\":0,\"bestseller\":0,\"publisher\":\"MIT Press\",\"edition\":\"3\",\"publicationYear\":\"2028\"}',  'test, book, test book',  11),
(11,  0,  'Testing in java: how does it work',  'Ilya Potemin', 0.00, '{\"reference\":0,\"bestseller\":1,\"publisher\":\"some publisher\",\"edition\":\"2\",\"publicationYear\":\"2018\"}', 'testing, java, howto', 9),
(14,  0,  'Teamwork with peace',  'Ilya Potemin', 4.20, '{\"reference\":0,\"bestseller\":1,\"publisher\":\"some publisher 2\",\"edition\":\"1\",\"publicationYear\":\"2028\"}', 'teamwork, wheregetpatience', 3),
(18,  0,  'Lombok for java development',  'Ilya Potemin', 19.84,  '{\"reference\":1,\"bestseller\":1,\"publisher\":\"some publisher 2\",\"edition\":\"1\",\"publicationYear\":\"2028\"}', 'development, lombok',  2),
(27,  0,  'Design Patterns: Elements of Reusable Object-Oriented Software', 'Erich Gamma, Ralph Johnson, John Vlissides, Richard Helm', 1700.00,  '{\"reference\":0,\"bestseller\":0,\"publisher\":\"Addison-Wesley Professional\",\"edition\":\"1\",\"publicationYear\":\"2003\"}',  'd2, awesome, bestseller',  3),
(28,  0,  'How to die before midterm',  'N. Shilov, Y. Kholodov', 23.00,  '{\"reference\":0,\"bestseller\":0,\"publisher\":\"KingBooks\",\"edition\":\"11\",\"publicationYear\":\"2018\"}', 'midterm, RIP, drop, calcooles',  609),
(29,  0,  'N.Shilov, the amazing illusionist',  'IU students',  1000.00,  '{\"reference\":1,\"bestseller\":1,\"publisher\":\"Awes-Team pub.\",\"edition\":\"1\",\"publicationYear\":\"2018\"}', 'shilov, theamazing', 0),
(30,  1,  'How to live on 12k', 'Ilya Potemin, Alexey Logachev',  12.00,  '{\"reference\":1,\"journalTitle\":\"Student Association Journal\",\"journalIssuePublicationDate\":\"2018-04-04\",\"journalIssueEditors\":\"Editor1, Editor2, ... , EditorN\"}',  'club12, howto',  10),
(31,  0,  'Introduction to Algorithms', 'Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest and Clifford Stein',  5000.00,  '{\"reference\":0,\"bestseller\":0,\"publisher\":\"MIT Press\",\"edition\":\"3\",\"publicationYear\":\"2009\"}',  'd1, cormen, algorithm',  0),
(32,  2,  'First AV material in our LMS', 'The Great Author', 1000.00,  '[]', 'av, nice, perfect, delivery2', 2),
(36,  2,  'Null References: The Billion Dollar Mistake',  'Tony Hoare', 700.00, '[]', 'd3, av, delivery3',  1),
(37,  1,  'Test for Ilya',  'Picroc', 12.00,  '{\"reference\":0,\"journalTitle\":\"Ilya and Android\",\"journalIssuePublicationDate\":\"2018-04-03\",\"journalIssueEditors\":\"Picroc, Valeriy\"}', 'test, ill, hey', 12),
(38,  0,  'aaa',  '11', 1.00, '{\"reference\":0,\"bestseller\":0,\"publisher\":\"pub\",\"edition\":\"1\",\"publicationYear\":\"2018\"}',  'aaa, bbb', 1),
(39,  2,  'aab',  '1asas',  1.00, '[]', 'key1, search', 1),
(40,  2,  'abb',  'ajdad',  11.00,  '[]', 'search, test', 1);

DROP TABLE IF EXISTS `librarians_permissions`;
CREATE TABLE `librarians_permissions` (
  `librarian_id` int(11) NOT NULL,
  `add` int(1) NOT NULL DEFAULT '0',
  `modify` int(1) NOT NULL DEFAULT '0',
  `remove` int(1) NOT NULL DEFAULT '0',
  UNIQUE KEY `librarian_id` (`librarian_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `librarians_permissions` (`librarian_id`, `add`, `modify`, `remove`) VALUES
(1, 1,  0,  1);

DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `text` text NOT NULL,
  `unread` int(1) NOT NULL DEFAULT '1',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `notifications` (`id`, `user_id`, `text`, `unread`, `time`) VALUES
(1, 6,  'Ку, это тест длинной строки в уведомлении, верните книгу', 0,  '2018-04-01 17:40:14'),
(2, 6,  'notification 2', 0,  '2018-04-23 07:23:25');

DROP TABLE IF EXISTS `queue`;
CREATE TABLE `queue` (
  `user_id` int(11) NOT NULL,
  `document_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `queue` (`user_id`, `document_id`) VALUES
(1, 31),
(5, 31),
(13,  29),
(1, 29);

DROP TABLE IF EXISTS `sessions`;
CREATE TABLE `sessions` (
  `user_id` int(11) NOT NULL,
  `access_token` varchar(100) NOT NULL,
  `expiration_date` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `access_token` (`access_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `sessions` (`user_id`, `access_token`, `expiration_date`) VALUES
(1, '4ec0e197e509c05679233f6dcf8f2434a2f65e98e345383cd40e1e51724401cf', 1524692205),
(5, '20826034e771bd7ee5f43f52fd25bc73f9fe55da7815a587ccd3910cb8a00350', 1524735351),
(6, 'd61b3d7b2f94d1c5a988fb500b75bf580c4886ed29da28e080ef70fdf9f89383', 1524737532);

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(100) NOT NULL,
  `card_UID` varchar(50) NOT NULL DEFAULT '',
  `type` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  `address` varchar(150) NOT NULL,
  `phone` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

INSERT INTO `users` (`id`, `password`, `card_UID`, `type`, `name`, `address`, `phone`) VALUES
(1, '64ccc3443652504bbced839035f2b195', 'BB8312C5EF08040001903D406D70141D', 0,  'Valerian', 'Not determined', '79998053149'),
(2, '64ccc3443652504bbced839035f2b195', '', 2,  'Some Bad Guy', 'Innopolis v2.0', 'hidden'),
(5, '64ccc3443652504bbced839035f2b195', '', 0,  'Ilya Potemin', 'My address', '88005553535'),
(6, '64ccc3443652504bbced839035f2b195', '', 1,  'Student dev3', 'address',  'phone'),
(8, '4cf4b91d76e40fd83feafcef1f99eeca', '', 0,  'Picroc', 'Universitetskaya', '89821759743222'),
(9, '898c6a04076466505e3c41e603f5684e', '', 0,  'Alexey', 'Innopolis',  '898288000228329'),
(11,  '1785e31356894edf01bdd59ccc99b7d0', '', 3,  'Shiloff',  'Universitetskay st. JK', '123456789111'),
(12,  '64ccc3443652504bbced839035f2b195', '', 3,  'Sergey Afonso',  'Via Margutta, 3',  '30001'),
(13,  '64ccc3443652504bbced839035f2b195', '', 3,  'Veronica Rama',  'Stret Atocha, 27', '112358'),
(15,  'f15a86261c143fab7f248c257cba47ac', '', 1,  'Alexey', 'Univ 1 1 312', '+798211111111');

-- 2018-04-23 10:43:15