-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 28, 2019 at 08:48 PM
-- Server version: 5.7.24
-- PHP Version: 7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `milestonerdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `milestone`
--

DROP TABLE IF EXISTS `milestone`;
CREATE TABLE IF NOT EXISTS `milestone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `title` varchar(64) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `startdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `intendeddate` datetime DEFAULT NULL,
  `isfinished` tinyint(1) NOT NULL DEFAULT '0',
  `enddate` datetime DEFAULT NULL,
  `project` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `milestone`
--

INSERT INTO `milestone` (`id`, `userid`, `title`, `description`, `startdate`, `intendeddate`, `isfinished`, `enddate`, `project`) VALUES
(26, 7, 'Milestone 1', 'Create a website', '2019-04-27 23:04:50', '2019-05-24 12:00:00', 1, '2019-04-27 23:08:44', 13),
(27, 7, 'Add content Milestone', 'Add content to website', '2019-04-27 23:05:42', '2019-05-26 12:00:00', 0, NULL, 13),
(28, 7, 'Publish Website', 'Upload website to hoasting', '2019-04-27 23:09:20', '2019-06-01 12:00:00', 0, NULL, 13);

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
CREATE TABLE IF NOT EXISTS `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `title` varchar(64) NOT NULL,
  `isshared` tinyint(1) NOT NULL DEFAULT '0',
  `sharevalue` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sharevalue` (`sharevalue`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `project`
--

INSERT INTO `project` (`id`, `user`, `title`, `isshared`, `sharevalue`) VALUES
(13, 7, 'New Project 1', 1, 'ABCD123123sdfsdf'),
(15, 7, 'Project2', 0, '5hPchZ5vmPktANs2');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `email` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `email`, `password`) VALUES
(7, 'Test01', 'Test1@gmail.com', '$2a$12$REQAYbEF/1UQ7Qo7X5TlrO711fdDj/x/gGiJcN7uAxVWfsKfJHthW');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
