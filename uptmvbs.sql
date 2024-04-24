-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 24, 2024 at 09:17 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `uptmvbs`
--

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `code` varchar(50) NOT NULL,
  `location` varchar(100) NOT NULL,
  `capacity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`id`, `code`, `location`, `capacity`) VALUES
(35, 'RN02', 'Mac Room', 20),
(37, 'RC01', 'Lecture Room', 40),
(38, 'RC02', 'Computer Lab', 30),
(39, 'RC03', 'Childhood Room', 15),
(40, 'RC04', 'Library', 80),
(41, 'RC05', 'Mac Lab', 25),
(42, 'RC06', 'Music Room', 10),
(43, 'RC07', 'Studio', 20);

-- --------------------------------------------------------

--
-- Table structure for table `room_bookings`
--

CREATE TABLE `room_bookings` (
  `id` int(11) NOT NULL,
  `room_code` varchar(50) NOT NULL,
  `room_name` varchar(100) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `booking_date` date NOT NULL,
  `booking_time` time NOT NULL,
  `booking_end` time NOT NULL,
  `room_purpose` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `room_bookings`
--

INSERT INTO `room_bookings` (`id`, `room_code`, `room_name`, `customer_name`, `phone_number`, `booking_date`, `booking_time`, `booking_end`, `room_purpose`) VALUES
(20, 'RC01', 'Childhood Room', 'ahmad', '112123', '2024-04-26', '14:30:00', '15:00:00', 'presentation'),
(22, 'RC01', 'Mac Room', 'Rahman', '01222222222', '2024-04-25', '10:00:00', '12:00:00', 'kuliah');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `first_name`, `last_name`, `password`) VALUES
(2, 'mawaradleena@email.com', 'Mawar Adleena', 'Rakan Rakan', '$2a$10$Oq8JDtDzTFW9YDsDdNEVPewSDecpUr67U4b/ei6pZWuXl3Vm0Q/fu'),
(3, 'madam@gmail.com', 'Madam Khairatul', 'Alyani', '$2a$10$vNKfq/z/b/UkfpDWguvGYOTl3OM.qC5jg7vCEi1JhMsfxqRFxQl4u');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `room_bookings`
--
ALTER TABLE `room_bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_room_code` (`room_code`),
  ADD KEY `idx_room_name` (`room_name`),
  ADD KEY `idx_customer_name` (`customer_name`);
ALTER TABLE `room_bookings` ADD FULLTEXT KEY `room_code` (`room_code`,`room_name`,`customer_name`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT for table `room_bookings`
--
ALTER TABLE `room_bookings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
