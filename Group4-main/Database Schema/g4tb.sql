-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Sep 01, 2022 at 10:32 PM
-- Server version: 5.7.36
-- PHP Version: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `g4tb`
--

-- --------------------------------------------------------

--
-- Table structure for table `registration`
--

DROP TABLE IF EXISTS `registration`;
CREATE TABLE IF NOT EXISTS `registration` (
  `firstName` varchar(120) NOT NULL,
  `lastName` varchar(120) NOT NULL,
  `dob` varchar(120) NOT NULL,
  `phone` varchar(120) NOT NULL,
  `username` varchar(120) NOT NULL,
  `pass` varchar(120) NOT NULL,
  `nationality` varchar(120) NOT NULL,
  `address` varchar(120) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `registration`
--

INSERT INTO `registration` (`firstName`, `lastName`, `dob`, `phone`, `username`, `pass`, `nationality`, `address`) VALUES
('Gideon', 'Akonnor', '2022-08-11', '0506568399', 'gideon', '123456@', 'Ghanaian', 'Accra'),
('Gideon', 'Akonnor ', '2022-08-02', '0506568399', 'hero', '123456', 'Ghana', 'Accra'),
('Michael', 'Adjei', '2022-08-02', '0506568399', 'michael', '123456', 'Ghana', 'Accra'),
('Fred', 'Ofori', '2009-07-31', '0242749259', 'fred', '1234@', 'Ghana', 'Koforidua'),
('kofi', 'yeboah', '2022-08-02', '026323234', 'kofi', '09876', 'ghana', 'asdrf'),
('Mercy', 'Akonnor ', '2004-11-10', '0545656520', 'mercy', '6520', 'Ghana', 'Nsawam');

-- --------------------------------------------------------

--
-- Table structure for table `transactionshistory`
--

DROP TABLE IF EXISTS `transactionshistory`;
CREATE TABLE IF NOT EXISTS `transactionshistory` (
  `username` varchar(120) NOT NULL,
  `category` varchar(120) NOT NULL,
  `fromm` varchar(120) NOT NULL,
  `too` varchar(120) NOT NULL,
  `amount` varchar(120) NOT NULL,
  `status` varchar(120) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transactionshistory`
--

INSERT INTO `transactionshistory` (`username`, `category`, `fromm`, `too`, `amount`, `status`) VALUES
('fred', 'Transfer', 'Bank', 'MoMo', '200.0', 'Successful'),
('fred', 'Transfer', 'Bank', 'MoMo', '100.0', 'Successful'),
('fred', 'Transfer', 'Bank', 'MoMo', '100.0', 'Successful'),
('fred', 'Withdrawal', 'Bank', '-', '200.0', 'Successful'),
('fred', 'Transfer', 'MoMo', 'Bank', '100.0', 'Succesful'),
('mercy', 'Transfer', 'Bank', 'MoMo', '300.0', 'Successful'),
('mercy', 'Withdrawal', 'MoMo', '-', '800.0', 'Failed'),
('mercy', 'Withdrawal', 'MoMo', '-', '800.0', 'Failed'),
('mercy', 'Withdrawal', 'MoMo', '-', '800.0', 'Failed'),
('mercy', 'Transfer', 'Bank', 'MoMo', '800.0', 'Successful');

-- --------------------------------------------------------

--
-- Table structure for table `wallet`
--

DROP TABLE IF EXISTS `wallet`;
CREATE TABLE IF NOT EXISTS `wallet` (
  `username` varchar(120) NOT NULL,
  `connectedBank` varchar(120) DEFAULT NULL,
  `bankBalance` varchar(120) DEFAULT NULL,
  `accountNumber` varchar(120) DEFAULT NULL,
  `bankPin` varchar(120) DEFAULT NULL,
  `connectedMoMo` varchar(120) DEFAULT NULL,
  `momoBalance` varchar(120) DEFAULT NULL,
  `momoNumber` varchar(120) DEFAULT NULL,
  `momoPin` varchar(120) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `wallet`
--

INSERT INTO `wallet` (`username`, `connectedBank`, `bankBalance`, `accountNumber`, `bankPin`, `connectedMoMo`, `momoBalance`, `momoNumber`, `momoPin`) VALUES
('fred', 'GCB', '1439.0', '123456789987', '1020', 'MTN', '1822.0', '0242749259', '4040'),
('mercy', 'ADB', '799.0', '10120304123', '1234', 'MTN', '2683.0', '0545656520', '6565');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
