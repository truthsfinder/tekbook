-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 07, 2018 at 11:14 AM
-- Server version: 10.1.31-MariaDB
-- PHP Version: 7.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id3237631_bsb_api`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `book_id` int(11) NOT NULL,
  `book_class` varchar(255) NOT NULL,
  `book_title` varchar(1000) NOT NULL,
  `book_description` varchar(1000) NOT NULL,
  `book_price` double NOT NULL,
  `book_status` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`book_id`, `book_class`, `book_title`, `book_description`, `book_price`, `book_status`) VALUES
(1, 'CCS123', 'Programming', '', 100, 'active'),
(2, 'Rizal011', 'Rizal Gwapo', '', 150, 'active'),
(3, 'Natsci011', 'BIOLOGY', '', 155, 'active'),
(4, 'CCS123', 'Programming', '', 150, 'active'),
(5, 'Natsci011', 'Test1', '', 160, 'active'),
(6, 'Rizal011', 'Gwapo Rizal', '', 155, 'sold'),
(7, 'CCS123', 'Programming', '', 145, 'active'),
(8, 'Rizal011', 'Rizal Shufo', '', 100, 'active'),
(9, 'Natsci011', 'Biology', '', 125, 'active'),
(10, 'CCS123', 'Programming', '', 95, 'sold'),
(11, 'Rizal011', 'Life of Jose Rizal', '', 140, 'active'),
(12, 'NatSci011', 'Biology', '', 125, 'active'),
(13, 'CCS123', 'Programming', '', 250, 'active'),
(14, 'Rizal011', 'Childhood of Rizal', '', 155, 'active'),
(15, 'NatSci011', 'Biology', '', 165, 'active'),
(16, 'Natsci012', 'Earth and Life Science', '', 200, 'active'),
(17, 'Math012', 'Algebra', '', 140, 'active'),
(18, 'NatSci012', 'Earth and Life Science', '', 125, 'active'),
(19, 'Math011', 'Algebra', '', 100, 'sold'),
(20, 'CCS123', 'Programming', '', 180, 'sold'),
(21, 'Philo012', 'Pursuit of Happiness', '', 130, 'active'),
(22, 'Math011', 'Algebra', '', 140, 'active'),
(23, 'Rizal011', 'Rizal is a true hero', '', 100, 'sold'),
(24, 'NatSci011', 'Biology', '', 185, 'active'),
(25, 'rizal01', 'rizal in pajamas', '', 100, 'sold'),
(26, 'CCS123', 'Programming', '', 135, 'sold'),
(27, 'Philo012', 'Pursuit of Happiness', '', 200, 'active'),
(28, 'MATH011', 'Algebra', '', 195, 'active'),
(29, 'Rizal012', 'Life of Rizal', '', 100, 'active'),
(30, 'CCS213', 'Networking', '', 200, 'sold'),
(31, 'CCS123', 'Programming1', '', 150, 'sold'),
(32, 'Philo012', 'Philosophy of Man', '', 160, 'sold'),
(33, 'Rizal011', 'Jose Rizal our Hero', '', 160, 'active'),
(34, 'Natsci011', 'Biology', '', 175, 'sold'),
(35, 'Natsci012', 'Earth and Life Science', '', 130, 'active'),
(36, 'ccs333', 'networking 100', '', 50, 'active'),
(37, 'ccs560', 'programming 5 million', '', 100, 'active'),
(38, 'hum011', 'art appreciation', '', 40, 'active'),
(39, 'pe101', 'physical education', '', 100, 'active'),
(40, 'psych011', 'psychology', '', 80, 'active'),
(41, 'kill011', 'how to be a hitman', '', 100000, 'active'),
(42, 'natsci011', 'nat sci', '', 58, 'sold');

-- --------------------------------------------------------

--
-- Table structure for table `reputation_votes`
--

CREATE TABLE `reputation_votes` (
  `reputation_votes_id` int(11) NOT NULL,
  `voter_id` int(11) NOT NULL,
  `voted_id` int(11) NOT NULL,
  `upvote` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `downvote` varchar(15) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `reputation_votes`
--

INSERT INTO `reputation_votes` (`reputation_votes_id`, `voter_id`, `voted_id`, `upvote`, `downvote`) VALUES
(1, 2, 1, 'FALSE', 'TRUE'),
(2, 3, 2, 'TRUE', 'FALSE'),
(3, 3, 1, 'TRUE', 'FALSE'),
(4, 4, 3, 'TRUE', 'FALSE'),
(5, 13, 1, 'FALSE', 'TRUE'),
(6, 17, 1, 'TRUE', 'FALSE');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `contact_number` varchar(255) NOT NULL,
  `password` varchar(1000) NOT NULL,
  `reputation` int(11) NOT NULL,
  `birthdate` date NOT NULL,
  `email_address` varchar(1000) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `profile_picture` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `lastname`, `firstname`, `contact_number`, `password`, `reputation`, `birthdate`, `email_address`, `gender`, `profile_picture`) VALUES
(1, 'jeff', 'BAYAWA', 'JEFF', '09959962351', 'd41d8cd98f00b204e9800998ecf8427e', 3, '1996-06-06', 'jeffbayawa@gmail.com', 'Male', 'chey.png'),
(2, 'mon', 'BINGCALAN', 'MONROE', '09999999999', 'e10adc3949ba59abbe56e057f20f883e', 2, '2000-01-01', 'mon@gmail.com', 'Male', 'shofo.png'),
(3, 'ranz', 'Antero', 'Lorenze', '09959480248', '827ccb0eea8a706c4c34a16891f84e7b', 2, '1996-11-27', 'ranz@gmail.com', 'Male', 'jcole.png'),
(4, 'ariel', 'JIMENEZ', 'ARIEL', '09936325594', 'd41d8cd98f00b204e9800998ecf8427e', 1, '1999-09-19', 'ariel@gmail.com', 'Male', 'chuyen.png'),
(5, 'test1', 'server', 'test1', '0999954286', 'd41d8cd98f00b204e9800998ecf8427e', 1, '2011-01-11', 'test1@gmail.com', 'Female', 'default.png'),
(12, 'test2', 'server', 'test2', '0994869215', 'd41d8cd98f00b204e9800998ecf8427e', 1, '2000-01-01', 'test2@gmail.com', 'Female', 'default.png'),
(13, 'damnkid', 'damn', 'kid', '', '202cb962ac59075b964b07152d234b70', 1, '0000-00-00', 'damnkid@gmail.com', '', 'default.png'),
(15, 'test3', 'Server', 'Test3', '09959492658', 'd41d8cd98f00b204e9800998ecf8427e', 1, '1999-09-09', 'test3@gmail.com', 'Select Gender', 'default.png'),
(16, 'test4', 'Server', 'Test', '', '827ccb0eea8a706c4c34a16891f84e7b', 1, '0000-00-00', 'test4@gmail.com', '', 'default.png'),
(17, 'demnkid', 'kid', 'damn', '0909090909', '202cb962ac59075b964b07152d234b70', 1, '0000-00-00', 'demnkid@gmail.com', 'Select Gender', 'default.png'),
(18, 'tester1', 'test', 'server', '', '098f6bcd4621d373cade4e832627b4f6', 1, '0000-00-00', 'testserver@gmail.com', '', 'default.png'),
(19, 'tester1', 'test', 'server', '', '098f6bcd4621d373cade4e832627b4f6', 1, '0000-00-00', 'testserver@gmail.com', '', 'default.png'),
(20, 'test', 'server', 'test', '', '202cb962ac59075b964b07152d234b70', 1, '0000-00-00', 'servertester@gmail.com', '', 'default.png'),
(21, 'johnwick', 'wick', 'john', '', '202cb962ac59075b964b07152d234b70', 1, '0000-00-00', 'dog@gmail.com', '', 'default.png'),
(22, 'renzen', 'Dev', 'App', '', 'd8578edf8458ce06fbc5bb76a58c5ca4', 1, '0000-00-00', 'renzen@gmail.com', '', 'default.png'),
(23, 'appdev', 'Dev', 'App', '', '827ccb0eea8a706c4c34a16891f84e7b', 1, '0000-00-00', 'appdev@gmail.com', '', 'default.png'),
(24, 'mon', 'bingcalan', 'monroe', '', '202cb962ac59075b964b07152d234b70', 1, '0000-00-00', 'mon@gmail.com', '', 'default.png'),
(25, 'jun', 'Antero', 'Jun', '', '827ccb0eea8a706c4c34a16891f84e7b', 1, '0000-00-00', 'jun@gmail.com', '', 'default.png'),
(26, 'imyspace', 'antero', 'lhore', '', 'c7c5152db06f6931ecef7c717915b2cc', 1, '0000-00-00', 'lhoreantero95@gmail.com', '', 'default.png'),
(27, 'david', 'huhay', 'hayo', '', '202cb962ac59075b964b07152d234b70', 1, '0000-00-00', 'david@gmail.com', '', 'default.png');

-- --------------------------------------------------------

--
-- Table structure for table `userbooks`
--

CREATE TABLE `userbooks` (
  `userbook_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `date_added` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userbooks`
--

INSERT INTO `userbooks` (`userbook_id`, `book_id`, `user_id`, `date_added`) VALUES
(1, 1, 1, '2018-05-24 06:49:20'),
(2, 2, 1, '2018-05-24 06:49:48'),
(3, 3, 1, '2018-05-24 06:50:17'),
(4, 4, 2, '2018-05-24 06:51:37'),
(5, 5, 2, '2018-05-24 06:51:56'),
(6, 6, 2, '2018-05-24 06:52:13'),
(7, 7, 3, '2018-05-24 06:58:37'),
(8, 8, 3, '2018-05-24 07:00:01'),
(9, 9, 3, '2018-05-24 07:01:27'),
(10, 10, 5, '2018-05-24 09:20:53'),
(11, 11, 5, '2018-05-24 09:21:47'),
(12, 12, 5, '2018-05-24 09:22:10'),
(13, 13, 4, '2018-05-24 09:33:00'),
(14, 14, 4, '2018-05-24 09:33:42'),
(15, 15, 4, '2018-05-24 09:35:27'),
(16, 16, 4, '2018-05-24 09:35:56'),
(17, 17, 4, '2018-05-24 09:36:44'),
(18, 18, 2, '2018-05-24 09:39:39'),
(19, 19, 2, '2018-05-24 09:40:45'),
(20, 20, 12, '2018-05-25 03:34:37'),
(21, 21, 12, '2018-05-25 03:35:17'),
(22, 22, 12, '2018-05-25 03:37:19'),
(23, 23, 12, '2018-05-25 03:37:57'),
(24, 24, 12, '2018-05-25 03:38:26'),
(25, 25, 13, '2018-05-25 12:28:02'),
(26, 26, 15, '2018-05-25 05:34:52'),
(27, 27, 15, '2018-05-25 05:35:27'),
(28, 28, 15, '2018-05-25 05:35:58'),
(29, 29, 15, '2018-05-25 05:37:23'),
(30, 30, 15, '2018-05-25 05:37:47'),
(31, 31, 16, '2018-05-26 12:56:10'),
(32, 32, 16, '2018-05-26 12:58:43'),
(33, 33, 16, '2018-05-26 12:59:23'),
(34, 34, 16, '2018-05-26 01:00:44'),
(35, 35, 16, '2018-05-26 01:01:30'),
(36, 36, 17, '2018-05-26 07:16:02'),
(37, 37, 17, '2018-05-26 07:16:45'),
(38, 38, 20, '2018-05-26 07:18:09'),
(39, 39, 20, '2018-05-26 07:18:28'),
(40, 40, 20, '2018-05-26 07:18:50'),
(41, 41, 21, '2018-05-26 07:20:34'),
(42, 42, 17, '2018-05-26 11:54:22');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`book_id`);

--
-- Indexes for table `reputation_votes`
--
ALTER TABLE `reputation_votes`
  ADD PRIMARY KEY (`reputation_votes_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `userbooks`
--
ALTER TABLE `userbooks`
  ADD PRIMARY KEY (`userbook_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `book_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `reputation_votes`
--
ALTER TABLE `reputation_votes`
  MODIFY `reputation_votes_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `userbooks`
--
ALTER TABLE `userbooks`
  MODIFY `userbook_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
