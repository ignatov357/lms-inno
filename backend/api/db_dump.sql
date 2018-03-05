-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Мар 05 2018 г., 13:28
-- Версия сервера: 5.6.31-77.0
-- Версия PHP: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `insight911_1`
--

-- --------------------------------------------------------

--
-- Структура таблицы `booked_documents`
--

CREATE TABLE IF NOT EXISTS `booked_documents` (
  `user_id` int(11) NOT NULL,
  `document_id` int(11) NOT NULL,
  `return_till` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `booked_documents`
--

INSERT INTO `booked_documents` (`user_id`, `document_id`, `return_till`) VALUES
(1, 26, 1521981162),
(2, 3, 1520433157),
(1, 13, 1521983076),
(1, 10, 1521980186),
(1, 11, 1521378263),
(5, 14, 1521978351),
(5, 10, 1521978250),
(6, 11, 1521441461),
(1, 14, 1521987525),
(9, 11, 1522653891),
(7, 10, 1522011723),
(7, 27, 1522020200);

-- --------------------------------------------------------

--
-- Структура таблицы `documents`
--

CREATE TABLE IF NOT EXISTS `documents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `authors` text NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `additional_info` text NOT NULL,
  `keywords` text NOT NULL,
  `instock_count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `documents`
--

INSERT INTO `documents` (`id`, `type`, `title`, `authors`, `price`, `additional_info`, `keywords`, `instock_count`) VALUES
(2, 0, 'Test Book 2', 'Ignatov Valery N., ...', '123.45', '{\"reference\":1,\"bestseller\":1,\"publisher\":\"MIT Press\",\"edition\":\"1\",\"publicationYear\":\"2018\"}', 'test, book, test book', 9),
(3, 0, 'Test Book 3', 'Ignatov Valery N., ...', '123.45', '{\"reference\":0,\"bestseller\":1,\"publisher\":\"MIT Press\",\"edition\":\"3\",\"publicationYear\":\"2019\"}', 'test, book, test book', 1),
(10, 0, 'Testing in java', 'Ilya Potemin', '0.00', '{\"reference\":0,\"bestseller\":0,\"publisher\":\"some publisher\",\"edition\":\"1\",\"publicationYear\":\"2018\"}', 'testing, java', 5),
(8, 0, 'Test Book 8', 'Ignatov Valery N., ...', '123.45', '{\"reference\":1,\"bestseller\":1,\"publisher\":\"MIT Press\",\"edition\":\"3\",\"publicationYear\":\"2028\"}', 'test, book, test book', 12),
(11, 0, 'Testing in java: how does it work', 'Ilya Potemin', '0.00', '{\"reference\":0,\"bestseller\":1,\"publisher\":\"some publisher\",\"edition\":\"2\",\"publicationYear\":\"2018\"}', 'testing, java, howto', 6),
(13, 0, 'Java: how to get rid of it', 'Ilya Potemin', '0.01', '{\"reference\":0,\"bestseller\":0,\"publisher\":\"some publisher 2\",\"edition\":\"2\",\"publicationYear\":\"1999\"}', 'java, againjava, edition2, javagoaway', 8),
(14, 0, 'Teamwork with peace', 'Ilya Potemin', '4.20', '{\"reference\":0,\"bestseller\":1,\"publisher\":\"some publisher 2\",\"edition\":\"1\",\"publicationYear\":\"2028\"}', 'teamwork, wheregetpatience', 1),
(28, 0, 'How to die before midterm', 'N. Shilov, Y. Kholodov', '23.00', '{\"reference\":0,\"bestseller\":1,\"publisher\":\"KingBooks\",\"edition\":\"5\",\"publicationYear\":\"2018\"}', 'midterm, RIP, drop, calcooles', 666),
(27, 0, 'Autobiography', 'Picroc', '200000.00', '{\"reference\":0,\"bestseller\":1,\"publisher\":\"Awes Proj\",\"edition\":\"2\",\"publicationYear\":\"3018\"}', 'awesome, bestseller', 1),
(18, 0, 'Lombok for java development', 'Ilya Potemin', '19.84', '{\"reference\":0,\"bestseller\":1,\"publisher\":\"some publisher 2\",\"edition\":\"1\",\"publicationYear\":\"2028\"}', 'development, lombok', 2),
(19, 0, 'Testing in java 3', 'Ilya Potemin', '1.00', '{\"reference\":0,\"bestseller\":0,\"publisher\":\"some publisher 2\",\"edition\":\"1\",\"publicationYear\":\"2028\"}', 'java, wont, go, away', 10),
(29, 0, 'N.Shilov, the amazing illusionist', 'IU students', '1000.00', '{\"reference\":0,\"bestseller\":1,\"publisher\":\"Awes-Team pub.\",\"edition\":\"1\",\"publicationYear\":\"2018\"}', 'shilov, theamazing', 1);

-- --------------------------------------------------------

--
-- Структура таблицы `document_types`
--

CREATE TABLE IF NOT EXISTS `document_types` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `document_types`
--

INSERT INTO `document_types` (`id`, `name`) VALUES
(0, 'Book'),
(1, 'Journal Article'),
(2, 'Audio/Video Material');

-- --------------------------------------------------------

--
-- Структура таблицы `sessions`
--

CREATE TABLE IF NOT EXISTS `sessions` (
  `user_id` int(11) NOT NULL,
  `access_token` varchar(100) NOT NULL,
  `expiration_date` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `access_token` (`access_token`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `sessions`
--

INSERT INTO `sessions` (`user_id`, `access_token`, `expiration_date`) VALUES
(8, '3cc27a25683c4cfad36330e8032fbb4c26391f32d5056d52c6a7309b88a87935', 1520469882),
(5, '978e0810f20b604a2c59af1fd03dd191f839ddb7e630a2c13a095d7506a36aca', 1520502097),
(1, '69bbc8253aa92d7e36968101882729a827ebae86c74ead3a015bd3fee1918a17', 1520504468),
(9, 'bf2968014115d4afd820578a2c1f652a7256bb4f165ca4c6356c748191693b15', 1520504364),
(6, 'd2e85d977014516884a355d8564d7d51d6e4a1a3a44c73eaed5ffd41a4d07a4d', 1520502790);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(100) NOT NULL,
  `card_UID` varchar(50) NOT NULL DEFAULT '',
  `type` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  `address` varchar(150) NOT NULL,
  `phone` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `password`, `card_UID`, `type`, `name`, `address`, `phone`) VALUES
(1, '64ccc3443652504bbced839035f2b195', 'BB8312C5EF08040001903D406D70141D', 2, 'Valerian', 'Not determined', '79998053149'),
(2, '64ccc3443652504bbced839035f2b195', '', 2, 'Some Bad Guy', 'Innopolis v2.0', 'hidden'),
(6, '64ccc3443652504bbced839035f2b195', '', 0, 'java test student', 'address', 'phone'),
(5, '64ccc3443652504bbced839035f2b195', '', 2, 'Ilya Potemin', 'My address', '88005553535'),
(9, '898c6a04076466505e3c41e603f5684e', '', 1, 'Alexey', 'Innopolis', '898288000228329'),
(8, '4cf4b91d76e40fd83feafcef1f99eeca', '', 1, 'Picroc', 'Universitetskaya', '89821759743222');

-- --------------------------------------------------------

--
-- Структура таблицы `user_types`
--

CREATE TABLE IF NOT EXISTS `user_types` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `user_types`
--

INSERT INTO `user_types` (`id`, `name`) VALUES
(0, 'Student'),
(1, 'Faculty Member'),
(2, 'Librarian');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
