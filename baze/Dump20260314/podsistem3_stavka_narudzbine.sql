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
-- Table structure for table `stavka_narudzbine`
--

DROP TABLE IF EXISTS `stavka_narudzbine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stavka_narudzbine` (
  `IdStavkaNar` int NOT NULL AUTO_INCREMENT,
  `IdNar` int NOT NULL,
  `IdArt` int NOT NULL,
  `IdProdavca` int NOT NULL,
  `Kolicina` int NOT NULL,
  `JedinicnaCena` decimal(12,2) NOT NULL,
  PRIMARY KEY (`IdStavkaNar`),
  KEY `IdNar_idx` (`IdNar`),
  CONSTRAINT `IdNar` FOREIGN KEY (`IdNar`) REFERENCES `narudzbina` (`IdNar`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stavka_narudzbine`
--

LOCK TABLES `stavka_narudzbine` WRITE;
/*!40000 ALTER TABLE `stavka_narudzbine` DISABLE KEYS */;
INSERT INTO `stavka_narudzbine` VALUES (1,4,3,3,2,899.99),(2,4,7,3,1,950.00),(3,5,7,3,1,950.00),(4,5,3,3,2,899.99),(5,6,7,3,1,760.00),(6,6,3,3,2,899.99),(7,7,4,3,2,599.99),(8,8,3,3,2,899.99),(9,8,2,3,2,1104.99),(10,8,1,3,1,699.99),(11,8,6,3,3,750.00);
/*!40000 ALTER TABLE `stavka_narudzbine` ENABLE KEYS */;
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
