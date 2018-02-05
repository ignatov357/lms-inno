SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `insight911_1`
--

-- --------------------------------------------------------

--
-- Structure of table `booked_documents`
--

CREATE TABLE IF NOT EXISTS `booked_documents` (
  `user_id` int(11) NOT NULL,
  `document_id` int(11) NOT NULL,
  `return_till` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure of table `documents`
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
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure of table `sessions`
--

CREATE TABLE IF NOT EXISTS `sessions` (
  `user_id` int(11) NOT NULL,
  `access_token` varchar(100) NOT NULL,
  `expiration_date` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `access_token` (`access_token`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure of table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(100) NOT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  `address` varchar(150) NOT NULL,
  `phone` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
