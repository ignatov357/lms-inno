-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Фев 02 2018 г., 21:06
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
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `documents`
--

INSERT INTO `documents` (`id`, `type`, `title`, `authors`, `price`, `additional_info`, `keywords`) VALUES
(1, 0, 'Book', 'Valerian Ignatov', '123.00', '[]', 'valera, book, ignatov');

-- --------------------------------------------------------

--
-- Структура таблицы `sessions`
--

CREATE TABLE IF NOT EXISTS `sessions` (
  `user_id` int(11) NOT NULL,
  `access_token` varchar(100) NOT NULL,
  `expiry_date` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `access_token` (`access_token`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `sessions`
--

INSERT INTO `sessions` (`user_id`, `access_token`, `expiry_date`) VALUES
(2, '3731fa43edc1b3b1bc64364057a5118cb38e67b1578cc472433a3e74829e11d6', 1517772754),
(1, '2706fbce0aff1d104dc22d4f84160df89e473d5229ef1aa16dae33eb86ff4ad4', 1517692884),
(3, 'd02875ec4942bb419830591c0292eadf645bac8e087933e5eddd25f75f4164e4', 1517851882),
(0, '0681e4e32c396d0670be1831f735e2a05f779e9236f64e71afb65c23bf845640', 1517851521);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(100) NOT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  `address` varchar(150) NOT NULL,
  `phone` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `password`, `type`, `name`, `address`, `phone`) VALUES
(1, '64ccc3443652504bbced839035f2b195', 2, 'Valerian', 'Not determined', '79998053149'),
(2, '6e713567cbdddbb964e0b8147fee20ae', 2, 'Some Bad Guy', 'Innopolis v2.0', 'hidden'),
(3, 'e7c25c482ebfdcf21145c0079e25c6db', 2, 'Ilya Potemin', 'My address', '88005553535');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
