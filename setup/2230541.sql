-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 28, 2025 at 11:52 AM
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
-- Database: `2230541`
--

-- --------------------------------------------------------

--
-- Table structure for table `application`
--

CREATE TABLE `application` (
  `application_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `internship_id` int(11) NOT NULL,
  `cv_url` varchar(255) DEFAULT NULL,
  `transcript_url` varchar(255) NOT NULL,
  `application_date` datetime NOT NULL DEFAULT current_timestamp(),
  `status` enum('pending','accepted','rejected','completed') NOT NULL DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `application`
--

INSERT INTO `application` (`application_id`, `student_id`, `internship_id`, `cv_url`, `transcript_url`, `application_date`, `status`) VALUES
(1, 2, 6, 'uploads/studentdocs/e8fc14a5-4620-4aca-a69a-e3ae4ae75e5e.pdf', 'uploads/studentdocs/908acf93-1bdd-43b6-809d-076101f27477.pdf', '2025-04-16 19:32:02', 'pending'),
(3, 7, 6, 'uploads/studentdocs/f4d05386-cd5e-4193-87b0-ac68c4de9a06.pdf', 'uploads/studentdocs/c4fbbaed-69b7-4c67-9f50-9d9445e3356a.pdf', '2025-04-16 19:36:36', 'pending'),
(4, 7, 5, 'uploads/studentdocs/41deb6f4-11d4-4f64-8726-29430cbdfafe.pdf', 'uploads/studentdocs/6b1636f1-b163-4312-946c-fda0d6eb424a.pdf', '2025-04-16 19:37:06', 'completed'),
(5, 2, 10, 'uploads/studentdocs/bfffb9a7-dcf2-47ac-bd3b-d4e1d5d0af01.pdf', 'uploads/studentdocs/86815ed9-bd09-417f-99d8-5adc717c0a2f.pdf', '2025-04-18 00:33:03', 'pending'),
(6, 2, 10, 'uploads/studentdocs/4fa3c4fa-bb56-4c26-bac8-9f007a8feac3.pdf', 'uploads/studentdocs/775876eb-d1e6-414e-96f6-046ce92d7c20.pdf', '2025-04-18 00:33:24', 'pending'),
(7, 2, 10, 'uploads/studentdocs/c25d100b-de7c-476b-ab19-a45bb61e72e9.pdf', 'uploads/studentdocs/407baeab-cd62-4467-a617-a4cf94c86724.pdf', '2025-04-18 00:35:09', 'pending');

-- --------------------------------------------------------

--
-- Table structure for table `company`
--

CREATE TABLE `company` (
  `company_id` int(11) NOT NULL,
  `company_name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `industry` enum('Information Technology','Finance & Banking','Healthcare & Pharmaceuticals','Education & Training','Telecommunications','Legal Services','Marketing & Advertising','Hospitality & Tourism') DEFAULT NULL,
  `website` varchar(128) DEFAULT NULL,
  `logo_url` varchar(255) DEFAULT NULL,
  `location` varchar(100) NOT NULL,
  `contact_info` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `company`
--

INSERT INTO `company` (`company_id`, `company_name`, `description`, `industry`, `website`, `logo_url`, `location`, `contact_info`) VALUES
(1, 'Microsoft', 'Microsoft Corporation is an American multinational corporation and technology conglomerate headquartered in Redmond, Washington.', 'Information Technology', 'https://www.microsoft.com', 'uploads/images/332314e4-4ec9-4731-8da2-dd788ee267fe.jpeg', ' Redmond, Washington, U.S.', '+266 22305454'),
(2, 'Reddit (PTY) LTD', 'Reddit gives you the best of the internet in one place. Get a constantly updating feed of breaking news, fun stories, pics, memes, and videos just for you. Passionate about something niche? …', 'Information Technology', 'www.reddit.com', 'uploads/images/a456fdc2-5dab-4827-b4c0-8a7b84392cd5.png', 'Manchekoaneng', '+266 22305849'),
(3, 'Joel Holdings (PTY) LTD', '', 'Marketing & Advertising', 'www.joelholdings.com', 'uploads/images/45e0d962-ee77-4588-bf64-a315d36e1341.png', 'Botha-Bothe', '+266 57650967'),
(4, 'Yahoo Lesotho', 'Something, I\'m lazy', 'Telecommunications', 'www.yahoo.com', 'uploads/images/1510d935-fecc-4bfc-bbf4-5910771e3370.png', 'Maseru', '+266 22599403');

-- --------------------------------------------------------

--
-- Table structure for table `document`
--

CREATE TABLE `document` (
  `document_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `documentType` enum('curriculum_vitae','academic_transcript','business_license','work_permit') DEFAULT NULL,
  `documentUrl` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `document`
--

INSERT INTO `document` (`document_id`, `user_id`, `documentType`, `documentUrl`) VALUES
(1, 3, 'business_license', 'uploads/documents/55ad8e55-9128-46ea-80b0-225059462c01.pdf'),
(2, 4, 'work_permit', 'uploads/documents/32a01bee-8647-442c-861b-1327b2a7f795.pdf'),
(3, 5, 'business_license', 'uploads/documents/c590be89-5506-4e69-bbc4-f26fc7902ebb.pdf'),
(4, 6, 'business_license', 'uploads/documents/5384625d-b5e3-4c3f-b85e-fed38972c297.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `feedback_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `internship_id` int(11) NOT NULL,
  `rating` varchar(50) NOT NULL,
  `comments` varchar(255) NOT NULL,
  `feedback_date` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`feedback_id`, `student_id`, `internship_id`, `rating`, `comments`, `feedback_date`) VALUES
(2, 7, 5, '4', 'Great workplace, great colleagues. I learned a lot regarding the advanced tools used in my field. Additionally, my interpersonal skills increased exponentially.', '2025-04-22 09:47:23');

-- --------------------------------------------------------

--
-- Table structure for table `feedback_reply`
--

CREATE TABLE `feedback_reply` (
  `reply_id` int(11) NOT NULL,
  `feedback_id` int(11) NOT NULL,
  `reply_text` text NOT NULL,
  `reply_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `feedback_reply`
--

INSERT INTO `feedback_reply` (`reply_id`, `feedback_id`, `reply_text`, `reply_date`) VALUES
(1, 2, 'Testing the first reply. Thank you Bokang, we try to serve interns with the best in the marketplace.', '2025-04-22 12:06:36'),
(2, 2, 'second reply. Testing something', '2025-04-22 12:11:05');

-- --------------------------------------------------------

--
-- Table structure for table `internship`
--

CREATE TABLE `internship` (
  `internship_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `category` enum('engineering','information technology','marketing','finance','human resources','design','sales','operations') NOT NULL,
  `location` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `posted_date` datetime NOT NULL DEFAULT current_timestamp(),
  `requirements` varchar(128) DEFAULT NULL,
  `student_id` int(11) DEFAULT NULL,
  `status` enum('open','closed') NOT NULL DEFAULT 'open'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `internship`
--

INSERT INTO `internship` (`internship_id`, `company_id`, `title`, `category`, `location`, `description`, `posted_date`, `requirements`, `student_id`, `status`) VALUES
(2, 2, 'Corporate Finance Intern', 'finance', 'Maseru', 'Support the finance team with budgeting, forecasting, and financial analysis. Assist in preparing monthly reports and help streamline internal financial processes.', '2025-04-16 19:02:06', 'Accounting, Critical Financial Analysis', NULL, 'open'),
(3, 3, 'UI/UX Design Intern', 'design', 'Botha-Bothe', 'Contribute to wireframes, user flows, and high-fidelity mockups. Collaborate with developers and designers to improve user interfaces and enhance overall user experience.', '2025-04-16 19:03:35', 'Figma, Sketch, Adobe Suite Tools, Design Principles', NULL, 'open'),
(4, 4, 'Network Engineering Intern', 'engineering', 'Remote', 'Support the IT team in maintaining and troubleshooting network systems. Help configure routers, switches, and firewalls while learning about network security and performance.', '2025-04-16 19:05:44', 'Cisco Packet Tracer, Wireless Networks, VLAN Segmeting', NULL, 'open'),
(5, 1, 'Mechanical Engineering Intern', 'engineering', 'Maseru', 'Assist in the design, prototyping, and testing of mechanical components. Work with CAD tools, participate in lab tests, and support documentation of engineering processes.', '2025-04-16 19:07:24', 'CAD, Archichad, Design Understanding', 7, 'open'),
(6, 3, 'Mobile Developer', 'information technology', 'Botha-Bothe', 'Design and develop cross-platform, fullstack applications that follow new arch principles', '2025-04-16 19:09:20', 'React Native, Expo, Android, Swift, Kotlin', NULL, 'open'),
(7, 4, 'Data Entry Intern', 'operations', 'Maseru', 'Plan and assist in collecting, formatting, and monitoring company and user records.', '2025-04-16 19:11:52', 'DBMS, Excel, Data Science', NULL, 'open'),
(8, 2, 'Investment Analyst Intern', 'marketing', 'Maseru, Downtown', 'Conduct market research, analyze financial statements, and support investment decisions. Work alongside analysts to evaluate stocks, bonds, and other assets.', '2025-04-16 19:13:44', 'Finance, Stocks Analysis', NULL, 'open'),
(9, 2, 'Java Developer', 'engineering', 'Maseru, Downtown', 'Assist in writing, testing, and debugging Java code. Collaborate with developers on web application projects, and contribute to feature development and bug fixing.', '2025-04-16 19:14:44', 'Java, MVC, OOP, JSTL', NULL, 'open'),
(10, 1, 'Health Information Manager', 'information technology', 'Remote', 'Assist with journal entries, account reconciliation, and invoice processing. Gain hands-on experience with bookkeeping and financial reporting under supervision.', '2025-04-16 19:17:05', 'Weka, OpenMRS, Medical Database, DBMS, Health', NULL, 'open');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL,
  `roleName` enum('admin','employer','student') NOT NULL DEFAULT 'student'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`role_id`, `roleName`) VALUES
(1, 'admin'),
(2, 'employer'),
(3, 'student');

-- --------------------------------------------------------

--
-- Table structure for table `userdetails`
--

CREATE TABLE `userdetails` (
  `detail_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `firstName` varchar(100) NOT NULL,
  `lastName` varchar(100) NOT NULL,
  `email` varchar(128) NOT NULL,
  `phoneNumber` varchar(100) NOT NULL,
  `address` varchar(255) NOT NULL,
  `profileImageUrl` varchar(255) DEFAULT NULL,
  `document_id` int(11) DEFAULT NULL,
  `company_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `userdetails`
--

INSERT INTO `userdetails` (`detail_id`, `user_id`, `firstName`, `lastName`, `email`, `phoneNumber`, `address`, `profileImageUrl`, `document_id`, `company_id`) VALUES
(1, 1, 'Kananelo', 'Joel', 'joel@careerbridge.com', '+266 57650967', 'Admin HQ', NULL, NULL, NULL),
(2, 2, 'Rethabile', 'Lebelo', 'lebelorethabile21@gmail.com', '+266 57839392', 'Ha-Abia', 'uploads/images/124ec819-9cbd-4b5a-9317-24ba8380eae3.jpg', NULL, NULL),
(3, 3, 'Bill', 'Gates', 'bill@microsoft.com', '+266 65940932', 'United States', 'uploads/images/41aa5f8a-8f9c-40ff-80bd-a3420bc911a9.jpeg', NULL, 1),
(4, 4, 'Poloko', 'Nkolanyane', 'polokonkolanyane92@gmail.com', '+266 68594939', 'Ha Sae', 'uploads/images/87ed7158-ae28-4aaa-ad77-1fd8dd083bf2.jpg', NULL, 2),
(5, 5, 'Thabang', 'Joel', 'thabansnr@gmail.com', '+266 58760969', 'Likileng, Botha-Bothe', 'uploads/images/3c93e76e-4004-4464-b75f-c24a8913af5b.png', NULL, 3),
(6, 6, 'Sasha', 'Dubois', 'sasha-jwd@proton.me', '+266 67584939', 'France', 'uploads/images/49c90f34-e7b7-483c-b4bc-d53e2310e1e0.jpg', NULL, 4),
(7, 7, 'Bokang', 'Mahlaka', 'bokangmahlaka226@gmail.com', '+266 56565406', 'Manchekoaneng', 'uploads/images/4c0a7b7a-1397-47b6-8527-9234b62fa3cb.jpg', NULL, NULL),
(9, 9, 'Testing', 'User', 'test@gmail.com', '+266 59594939', 'Ha-Abia, Tlokoeng', 'uploads/images/2432a19f-70fb-47a7-ac02-fd9022717117.png', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `email` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `password`, `role_id`) VALUES
(1, 'kananeloj12@gmail.com', '$2a$10$HDjUv3BTN0Y9jiQchaAp9ecPdKp9QtOpHPofhwoV5/gRNiAPAEzNW', 1),
(2, 'lebelorethabile21@gmail.com', '$2a$10$ir1Yr0nhmqFgmsOMrLKuSeCFYsO4WqIec3bRav6v2ro.qgtN/UdVC', 3),
(3, 'bill@microsoft.com', '$2a$10$nJpO9QG04PA1K75Mc/eVye5XGq26Flk74cRhcjS0fiChKx6KEG7qi', 2),
(4, 'polokonkolanyane92@gmail.com', '$2a$10$ssy8d1CSJdNnBCn7NSSyyugPNaMfGwmbTRfD9KF.hpwaj9jb2SjAO', 2),
(5, 'thabansnr@gmail.com', '$2a$10$7ukFu1lMYMVO1mMqbHUkoukzX9NLWm9E1Enk67rRkNywQIZxfpHwW', 2),
(6, 'sasha-jwd@proton.me', '$2a$10$VHWvHhxbZZ2SGWpwcHYeN.cQXRBztou6.cNQxvg47ltJNDiwINSw6', 2),
(7, 'bokangmahlaka226@gmail.com', '$2a$10$vY6ujMEXcBJIbqqvnV.4eu78/Y2DlDcrpCLkPbhlSTd43kz37ob/a', 3),
(9, 'test@gmail.com', '$2a$10$sDCJ5HbtnhKQEQFHasXvUuC.NAwsXDLxCN8n/t1096.fz79gIuZiW', 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `application`
--
ALTER TABLE `application`
  ADD PRIMARY KEY (`application_id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `internship_id` (`internship_id`);

--
-- Indexes for table `company`
--
ALTER TABLE `company`
  ADD PRIMARY KEY (`company_id`);

--
-- Indexes for table `document`
--
ALTER TABLE `document`
  ADD PRIMARY KEY (`document_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`feedback_id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `internship_id` (`internship_id`);

--
-- Indexes for table `feedback_reply`
--
ALTER TABLE `feedback_reply`
  ADD PRIMARY KEY (`reply_id`),
  ADD KEY `feedback_id` (`feedback_id`);

--
-- Indexes for table `internship`
--
ALTER TABLE `internship`
  ADD PRIMARY KEY (`internship_id`),
  ADD KEY `company_id` (`company_id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `userdetails`
--
ALTER TABLE `userdetails`
  ADD PRIMARY KEY (`detail_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `document_id` (`document_id`),
  ADD KEY `company_id` (`company_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `users_ibfk_1` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `application`
--
ALTER TABLE `application`
  MODIFY `application_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `company`
--
ALTER TABLE `company`
  MODIFY `company_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `document`
--
ALTER TABLE `document`
  MODIFY `document_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `feedback_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `feedback_reply`
--
ALTER TABLE `feedback_reply`
  MODIFY `reply_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `internship`
--
ALTER TABLE `internship`
  MODIFY `internship_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `userdetails`
--
ALTER TABLE `userdetails`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `application`
--
ALTER TABLE `application`
  ADD CONSTRAINT `application_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `application_ibfk_2` FOREIGN KEY (`internship_id`) REFERENCES `internship` (`internship_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `document`
--
ALTER TABLE `document`
  ADD CONSTRAINT `document_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `feedback_ibfk_2` FOREIGN KEY (`internship_id`) REFERENCES `internship` (`internship_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `feedback_reply`
--
ALTER TABLE `feedback_reply`
  ADD CONSTRAINT `feedback_reply_ibfk_1` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`feedback_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `internship`
--
ALTER TABLE `internship`
  ADD CONSTRAINT `internship_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `internship_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `userdetails`
--
ALTER TABLE `userdetails`
  ADD CONSTRAINT `userdetails_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `userdetails_ibfk_2` FOREIGN KEY (`document_id`) REFERENCES `document` (`document_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `userdetails_ibfk_3` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
