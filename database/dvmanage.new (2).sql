-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: dvmanage
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `tblbankaccount`
--

DROP TABLE IF EXISTS `tblbankaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblbankaccount` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `BankName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AccountName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AccountNumber` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblbankaccount`
--

LOCK TABLES `tblbankaccount` WRITE;
/*!40000 ALTER TABLE `tblbankaccount` DISABLE KEYS */;
/*!40000 ALTER TABLE `tblbankaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblchartofaccount`
--

DROP TABLE IF EXISTS `tblchartofaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblchartofaccount` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ChartCode` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ChartName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblchartofaccount`
--

LOCK TABLES `tblchartofaccount` WRITE;
/*!40000 ALTER TABLE `tblchartofaccount` DISABLE KEYS */;
INSERT INTO `tblchartofaccount` VALUES (1,'1000','ASSETS'),(2,'1100','CURRENT ASSETS'),(3,'1101','CASH IN BANK(BDO)'),(4,'1102','REVOLVING FUNDS'),(5,'1103','ACCOUNTS RECEIVABLE'),(6,'1104','NOTES RECEIVABLE'),(7,'1105','ADVANCES FROM EMPLOYEES'),(8,'1106','MERCHANDISE INVENTORY'),(9,'1107','PREPAID INSURANCE'),(10,'1108','OTHER PREPAYMENTS'),(11,'1109','OFFICE SUPPLIES'),(12,'1200','NON-CURRENT ASSETS'),(13,'1201','LAND'),(14,'1202','BUILDING'),(15,'1203','OFFICE EQUIPMENT'),(16,'1204','TRANSPORTATION EQUIPMENT'),(17,'1205','FURNITURES AND FIXTURES');
/*!40000 ALTER TABLE `tblchartofaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblclient`
--

DROP TABLE IF EXISTS `tblclient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblclient` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ClientName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TIN` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblclient`
--

LOCK TABLES `tblclient` WRITE;
/*!40000 ALTER TABLE `tblclient` DISABLE KEYS */;
/*!40000 ALTER TABLE `tblclient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblcompany`
--

DROP TABLE IF EXISTS `tblcompany`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblcompany` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CompanyName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblcompany`
--

LOCK TABLES `tblcompany` WRITE;
/*!40000 ALTER TABLE `tblcompany` DISABLE KEYS */;
INSERT INTO `tblcompany` VALUES (1,'GPJAN CACAO FARM'),(2,'SAMPLE COMPANY 2'),(6,'SAMPLE COMPANY 3');
/*!40000 ALTER TABLE `tblcompany` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbldisbursementvoucher`
--

DROP TABLE IF EXISTS `tbldisbursementvoucher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbldisbursementvoucher` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DisbursementCode` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ChargeTo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Payee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TINNumber` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `FundSource` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CheckNo` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TotalAmount` decimal(11,2) DEFAULT NULL,
  `Date` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbldisbursementvoucher`
--

LOCK TABLES `tbldisbursementvoucher` WRITE;
/*!40000 ALTER TABLE `tbldisbursementvoucher` DISABLE KEYS */;
INSERT INTO `tbldisbursementvoucher` VALUES (1,'DVC2021031','GPJAN CACAO FARM','LOGITECH','1234567890','CASH IN BANK(BDO)','To Setup Revolving Fund','1244',1000.00,'3/20/21'),(2,'DVC2021032','GPJAN CACAO FARM','KARLMARX','1234567890','REVOLVING FUNDS','Revolving Fund Replenishment','345',300.00,'3/20/21');
/*!40000 ALTER TABLE `tbldisbursementvoucher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbldisbursementvoucherparticular`
--

DROP TABLE IF EXISTS `tbldisbursementvoucherparticular`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbldisbursementvoucherparticular` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DisbursementCode` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Amount` decimal(11,2) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbldisbursementvoucherparticular`
--

LOCK TABLES `tbldisbursementvoucherparticular` WRITE;
/*!40000 ALTER TABLE `tbldisbursementvoucherparticular` DISABLE KEYS */;
INSERT INTO `tbldisbursementvoucherparticular` VALUES (1,'DVC2021031','1102','REVOLVING FUNDS',1000.00),(2,'DVC2021032','1109','OFFICE SUPPLIES',100.00),(3,'DVC2021032','1200','NON-CURRENT ASSETS',100.00),(4,'DVC2021032','1201','LAND',100.00);
/*!40000 ALTER TABLE `tbldisbursementvoucherparticular` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblitems`
--

DROP TABLE IF EXISTS `tblitems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblitems` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Item` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Unit` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UnitCost` decimal(11,2) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblitems`
--

LOCK TABLES `tblitems` WRITE;
/*!40000 ALTER TABLE `tblitems` DISABLE KEYS */;
INSERT INTO `tblitems` VALUES (1,'1530','CHICKEN DUNG','TRUCK',10000.00),(2,'1530','CHICKEN DUNG','TRUCK',5.00);
/*!40000 ALTER TABLE `tblitems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbljournal`
--

DROP TABLE IF EXISTS `tbljournal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbljournal` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Date` varchar(45) DEFAULT NULL,
  `Reference` varchar(45) DEFAULT NULL,
  `Code` varchar(45) DEFAULT NULL,
  `Account` varchar(100) DEFAULT NULL,
  `Debit` decimal(11,2) DEFAULT NULL,
  `Credit` decimal(11,2) DEFAULT NULL,
  `Balance` decimal(11,2) DEFAULT NULL,
  `Status` varchar(45) DEFAULT NULL,
  `Company` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbljournal`
--

LOCK TABLES `tbljournal` WRITE;
/*!40000 ALTER TABLE `tbljournal` DISABLE KEYS */;
INSERT INTO `tbljournal` VALUES (1,'3/20/21','DVC2021031','1102','REVOLVING FUNDS',1000.00,300.00,700.00,'OPEN','GPJAN CACAO FARM'),(2,'3/20/21','DVC2021031','1101','CASH IN BANK(BDO)',0.00,1000.00,0.00,'OPEN','GPJAN CACAO FARM'),(3,'3/20/21','DVC2021032','1109','OFFICE SUPPLIES',100.00,0.00,0.00,'OPEN','GPJAN CACAO FARM'),(4,'3/20/21','DVC2021032','1200','NON-CURRENT ASSETS',100.00,0.00,0.00,'OPEN','GPJAN CACAO FARM'),(5,'3/20/21','DVC2021032','1201','LAND',100.00,0.00,0.00,'OPEN','GPJAN CACAO FARM');
/*!40000 ALTER TABLE `tbljournal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblrevolvingfund`
--

DROP TABLE IF EXISTS `tblrevolvingfund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblrevolvingfund` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `RFCode` varchar(45) DEFAULT NULL,
  `Bank` varchar(100) DEFAULT NULL,
  `Description` varchar(100) DEFAULT NULL,
  `Amount` decimal(11,2) DEFAULT NULL,
  `Status` varchar(45) DEFAULT NULL,
  `Date` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblrevolvingfund`
--

LOCK TABLES `tblrevolvingfund` WRITE;
/*!40000 ALTER TABLE `tblrevolvingfund` DISABLE KEYS */;
/*!40000 ALTER TABLE `tblrevolvingfund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblsupplier`
--

DROP TABLE IF EXISTS `tblsupplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblsupplier` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `SupplierName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TIN` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblsupplier`
--

LOCK TABLES `tblsupplier` WRITE;
/*!40000 ALTER TABLE `tblsupplier` DISABLE KEYS */;
INSERT INTO `tblsupplier` VALUES (1,'LOGITECH','1234567890'),(2,'KARLMARX','1234567890'),(5,'ME','2345678');
/*!40000 ALTER TABLE `tblsupplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblunit`
--

DROP TABLE IF EXISTS `tblunit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblunit` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Unit` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblunit`
--

LOCK TABLES `tblunit` WRITE;
/*!40000 ALTER TABLE `tblunit` DISABLE KEYS */;
INSERT INTO `tblunit` VALUES (1,'KG'),(2,'PC'),(3,'TRUCK'),(4,'LITRE'),(6,'DF');
/*!40000 ALTER TABLE `tblunit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbluser`
--

DROP TABLE IF EXISTS `tbluser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbluser` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `UserType` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Username` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbluser`
--

LOCK TABLES `tbluser` WRITE;
/*!40000 ALTER TABLE `tbluser` DISABLE KEYS */;
INSERT INTO `tbluser` VALUES (2,'ADMINISTRATOR','ADMIN','21232f297a57a5a743894a0e4a801fc3'),(3,'ADMINISTRATOR','ADMIN','13f9a75b59377aa80f3d'),(4,'ADMINISTRATOR','USER','121354f7021c564036312');
/*!40000 ALTER TABLE `tbluser` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-03-20 17:50:16
