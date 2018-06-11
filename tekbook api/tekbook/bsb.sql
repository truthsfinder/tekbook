-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 06, 2017 at 08:36 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 7.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bsb`
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
  `book_price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`book_id`, `book_class`, `book_title`, `book_description`, `book_price`) VALUES
(1, 'CCS120', 'Computer Fundamentals', 'Book for computer students', 500),
(2, 'CCS130', 'Test Book', 'This is only a test book', 100),
(17, 'Ccs100', 'Fundamentals of myself', '', 14.25),
(18, 'Ccs100', 'Fundamentals of myself', '', 14.25),
(19, 'Ccs100', 'Fundamentals of myself', '', 14.25),
(22, 'Ccs111', 'book1', '', 25.23),
(23, 'P.E. 101', 'Physical Education', '', 200.5),
(25, 'PE101', 'fundamentals', '', 25.33),
(26, 'Eng012', 'English 2', '', 125.55),
(27, 'ccs121', 'funda', '', 120.54),
(28, 'Hola', 'espanya', '', 211),
(29, 'Ccc', 'ccc', '', 122.4),
(30, 'test', 'teat', '', 242),
(31, 'test2', 'test2', '', 2424),
(32, 'test3', 'test3', '', 24),
(33, 'test4', 'teat4', '', 244),
(34, 'test5', 'test5', '', 244),
(35, 'test6', 'test6', '', 24),
(38, 'Engl012', 'English', '', 100.5);

-- --------------------------------------------------------

--
-- Table structure for table `reputation_votes`
--

CREATE TABLE `reputation_votes` (
  `reputation_votes_id` int(11) NOT NULL,
  `voter_id` int(11) NOT NULL,
  `voted_id` int(11) NOT NULL,
  `upvote` varchar(15) NOT NULL,
  `downvote` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reputation_votes`
--

INSERT INTO `reputation_votes` (`reputation_votes_id`, `voter_id`, `voted_id`, `upvote`, `downvote`) VALUES
(40, 1, 2, 'TRUE', 'FALSE'),
(41, 1, 3, 'TRUE', 'FALSE'),
(42, 4, 1, 'TRUE', 'FALSE'),
(43, 4, 2, 'TRUE', 'FALSE'),
(44, 4, 3, 'TRUE', 'FALSE'),
(45, 1, 4, 'TRUE', 'FALSE'),
(46, 5, 2, 'TRUE', 'FALSE'),
(47, 5, 1, 'FALSE', 'TRUE'),
(48, 5, 3, 'FALSE', 'TRUE');

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
(1, 'jwtaniza', 'Tan', 'John', '09752512055', '202cb962ac59075b964b07152d234b70', 17, '1993-12-30', 'chrisrenconiza@gmail.com', 'Male', 'john.png'),
(2, 'war', 'Taniza', 'John', '09752512044', '202cb962ac59075b964b07152d234b70', 34, '1993-12-29', 'jw.taniza@gmail.com', 'Male', 'warren.png'),
(3, 'qw', 'qw', 'qw', '09752512044', '006d2143154327a64d86a264aea225f3', 13, '0000-00-00', '', '', 'default.png'),
(4, 'qwerty', 'qwerty', 'qwerty', '9752512044', '202cb962ac59075b964b07152d234b70', 2, '0000-00-00', '', '', 'default.png'),
(5, 'test1', 'test', 'test', '9752512044', '202cb962ac59075b964b07152d234b70', 1, '2017-12-29', 'jw.taniza@gmail.com', 'Male', 'warren.png');

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
(1, 1, 1, '2017-09-24 00:00:00'),
(2, 2, 1, '2017-09-24 00:00:00'),
(4, 19, 2, '2017-09-24 11:08:12'),
(7, 22, 2, '2017-09-25 07:48:21'),
(8, 23, 1, '2017-09-25 09:04:28'),
(10, 25, 3, '2017-09-25 10:35:12'),
(11, 26, 1, '2017-09-29 11:40:18'),
(12, 27, 3, '2017-10-04 12:43:19'),
(13, 28, 3, '2017-10-04 12:47:38'),
(14, 29, 1, '2017-10-04 12:51:35'),
(15, 30, 1, '2017-10-04 01:03:01'),
(16, 31, 1, '2017-10-04 01:04:18'),
(17, 32, 1, '2017-10-04 01:04:44'),
(18, 33, 1, '2017-10-04 01:05:14'),
(19, 34, 1, '2017-10-04 01:05:38'),
(20, 35, 1, '2017-10-04 01:06:15'),
(23, 38, 5, '2017-10-04 08:15:24');

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
  MODIFY `book_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;
--
-- AUTO_INCREMENT for table `reputation_votes`
--
ALTER TABLE `reputation_votes`
  MODIFY `reputation_votes_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `userbooks`
--
ALTER TABLE `userbooks`
  MODIFY `userbook_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
