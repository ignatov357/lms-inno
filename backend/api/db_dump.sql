-- phpMyAdmin SQL Dump
-- version 4.7.8
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Апр 04 2018 г., 16:31
-- Версия сервера: 5.6.39-83.1
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
  `return_till` int(11) NOT NULL,
  `renewed` int(11) NOT NULL DEFAULT '0',
  `asked_to_return` int(11) NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `booked_documents`
--

INSERT INTO `booked_documents` (`user_id`, `document_id`, `return_till`, `renewed`, `asked_to_return`) VALUES
(1, 28, 1527684829, 1, 0),
(1, 14, 1527684622, 1, 0),
(1, 11, 1527684388, 1, 0);

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
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `documents`
--

INSERT INTO `documents` (`id`, `type`, `title`, `authors`, `price`, `additional_info`, `keywords`, `instock_count`) VALUES
(36, 2, 'Null References: The Billion Dollar Mistake', 'Tony Hoare', '700.00', '[]', 'd3, av, delivery3', 1),
(8, 0, 'Test Book 8', 'Ignatov Valery N., ...', '123.45', '{\"reference\":0,\"bestseller\":0,\"publisher\":\"MIT Press\",\"edition\":\"3\",\"publicationYear\":\"2028\"}', 'test, book, test book', 12),
(11, 0, 'Testing in java: how does it work', 'Ilya Potemin', '0.00', '{\"reference\":0,\"bestseller\":1,\"publisher\":\"some publisher\",\"edition\":\"2\",\"publicationYear\":\"2018\"}', 'testing, java, howto', 8),
(13, 0, 'Java: how to get rid of it', 'Ilya Potemin', '12.00', '{\"reference\":1,\"bestseller\":0,\"publisher\":\"some publisher 2\",\"edition\":\"2\",\"publicationYear\":\"1999\"}', 'java, againjava, edition2, javagoaway', 9),
(14, 0, 'Teamwork with peace', 'Ilya Potemin', '4.20', '{\"reference\":0,\"bestseller\":1,\"publisher\":\"some publisher 2\",\"edition\":\"1\",\"publicationYear\":\"2028\"}', 'teamwork, wheregetpatience', 2),
(28, 0, 'How to die before midterm', 'N. Shilov, Y. Kholodov', '23.00', '{\"reference\":0,\"bestseller\":0,\"publisher\":\"KingBooks\",\"edition\":\"11\",\"publicationYear\":\"2018\"}', 'midterm, RIP, drop, calcooles', 609),
(27, 0, 'Design Patterns: Elements of Reusable Object-Oriented Software', 'Erich Gamma, Ralph Johnson, John Vlissides, Richard Helm', '1700.00', '{\"reference\":0,\"bestseller\":0,\"publisher\":\"Addison-Wesley Professional\",\"edition\":\"1\",\"publicationYear\":\"2003\"}', 'd2, awesome, bestseller', 3),
(18, 0, 'Lombok for java development', 'Ilya Potemin', '19.84', '{\"reference\":1,\"bestseller\":1,\"publisher\":\"some publisher 2\",\"edition\":\"1\",\"publicationYear\":\"2028\"}', 'development, lombok', 2),
(29, 0, 'N.Shilov, the amazing illusionist', 'IU students', '1000.00', '{\"reference\":1,\"bestseller\":0,\"publisher\":\"Awes-Team pub.\",\"edition\":\"1\",\"publicationYear\":\"2018\"}', 'shilov, theamazing', 1),
(30, 1, 'How to live on 12k', 'Ilya Potemin, Alexey Logachev', '12.00', '{\"reference\":1,\"journalTitle\":\"Student Association Journal\",\"journalIssuePublicationDate\":\"2018-04-04\",\"journalIssueEditors\":\"Editor1, Editor2, ... , EditorN\"}', 'club12, howto', 10),
(31, 0, 'Introduction to Algorithms', 'Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest and Clifford Stein', '5000.00', '{\"reference\":1,\"bestseller\":0,\"publisher\":\"MIT Press\",\"edition\":\"3\",\"publicationYear\":\"2009\"}', 'd1, cormen, algorithm', 0),
(32, 2, 'First AV material in our LMS', 'The Great Author', '1000.00', '[]', 'av, nice, perfect, delivery2', 3);

-- --------------------------------------------------------

--
-- Структура таблицы `notifications`
--

CREATE TABLE IF NOT EXISTS `notifications` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `text` text NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `notifications`
--

INSERT INTO `notifications` (`id`, `user_id`, `text`, `time`) VALUES
(0, 1, 'Hello.', '2018-04-01 17:40:14');

-- --------------------------------------------------------

--
-- Структура таблицы `queue`
--

CREATE TABLE IF NOT EXISTS `queue` (
  `user_id` int(11) NOT NULL,
  `document_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `queue`
--

INSERT INTO `queue` (`user_id`, `document_id`) VALUES
(6, 31),
(1, 36);

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
(5, '7ff19b64c70d06d53b3628dc36e43a34a608eb7bfe9145cad39543420258762e', 1523105327),
(1, 'db8f0a181ed248aae8e84cca494933daabf72e1dc0575cee19109781e0a7ae2a', 1523106268),
(9, '68a83874680c0b61952ec0066597f29f528da6f876e6d16696641c2918ce08d9', 1523106736),
(6, '4361c34d54f1d1b9dafc9ab8afee960a1055d1d5a2555c01fdb040a021117737', 1523105678),
(13, '00fdeab77e5036d96389c6bd823dc0ea99c5ac0e33572c8028e8fd81b8700fde', 1523062230),
(2, 'a137b5d4125dae3cfa67e95fe590e9deb07d7948ac99713df56ac42a57f306fe', 1522940608);

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
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `password`, `card_UID`, `type`, `name`, `address`, `phone`) VALUES
(1, '64ccc3443652504bbced839035f2b195', 'BB8312C5EF08040001903D406D70141D', 2, 'Valerian', 'Not determined', '79998053149'),
(2, '64ccc3443652504bbced839035f2b195', '', 2, 'Some Bad Guy', 'Innopolis v2.0', 'hidden'),
(6, '64ccc3443652504bbced839035f2b195', '', 1, 'Student dev3', 'address', 'phone'),
(5, '64ccc3443652504bbced839035f2b195', '', 0, 'Ilya Potemin', 'My address', '88005553535'),
(9, '898c6a04076466505e3c41e603f5684e', '', 0, 'Alexey', 'Innopolis', '898288000228329'),
(8, '4cf4b91d76e40fd83feafcef1f99eeca', '', 0, 'Picroc', 'Universitetskaya', '89821759743222'),
(11, '1785e31356894edf01bdd59ccc99b7d0', '', 3, 'Shiloff', 'Universitetskay st. JK', '123456789111'),
(12, '64ccc3443652504bbced839035f2b195', '', 3, 'Sergey Afonso', 'Via Margutta, 3', '30001'),
(13, '64ccc3443652504bbced839035f2b195', '', 3, 'Veronica Rama', 'Stret Atocha, 27', '112358'),
(15, 'f15a86261c143fab7f248c257cba47ac', '', 1, 'Alexey', 'Univ 1 1 312', '+798211111111');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
