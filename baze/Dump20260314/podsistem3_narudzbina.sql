-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem3
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `narudzbina`
--

DROP TABLE IF EXISTS `narudzbina`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `narudzbina` (
  `IdNar` int NOT NULL AUTO_INCREMENT,
  `IdKupca` int NOT NULL,
  `UkupnaCena` decimal(12,2) NOT NULL,
  `VremeNarudzbine` datetime NOT NULL,
  `AdresaDostave` varchar(45) NOT NULL,
  `GradDostave` varchar(45) NOT NULL,
  PRIMARY KEY (`IdNar`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `narudzbina`
--

LOCK TABLES `narudzbina` WRITE;
/*!40000 ALTER TABLE `narudzbina` DISABLE KEYS */;
INSERT INTO `narudzbina` VALUES (4,5,2749.98,'2026-03-14 03:32:30','Konjarnik 78c','Sabac'),(5,5,2749.98,'2026-03-14 03:33:49','Konjarnik 78c','Sabac'),(6,5,2559.98,'2026-03-14 09:39:39','Konjarnik 78c','Sabac'),(7,5,1199.98,'2026-03-14 09:50:07','Konjarnik 78c','Sabac'),(8,1,6959.95,'2026-03-14 09:56:13','Medak 61','Sabac');
/*!40000 ALTER TABLE `narudzbina` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-14 11:28:37
