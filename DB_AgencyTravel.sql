-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: agency_travel1
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `id_booking` int NOT NULL AUTO_INCREMENT,
  `id_client` int NOT NULL,
  `id_tours` int NOT NULL,
  `people_count` int NOT NULL,
  `booking_date` timestamp NOT NULL,
  `start_date` date NOT NULL,
  `price_all_count` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_booking`,`id_client`,`id_tours`),
  KEY `booking_client_idx` (`id_client`),
  KEY `booking_tours_idx` (`id_tours`),
  CONSTRAINT `booking_client` FOREIGN KEY (`id_client`) REFERENCES `client_agency` (`id_client`),
  CONSTRAINT `booking_tours` FOREIGN KEY (`id_tours`) REFERENCES `tours_travel` (`id_tours`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (1,1,200,3,'2025-04-23 05:01:04','2025-04-30',2850.00),(2,1,200,4,'2025-04-23 05:17:52','2025-04-30',3800.00),(3,1,100,1,'2025-04-24 08:16:22','2025-04-26',1200.00),(4,7,200,4,'2025-04-24 08:26:14','2025-04-28',3800.00),(5,1,702,1,'2025-04-24 09:04:14','2025-04-25',750.00),(6,14,600,1,'2025-04-24 09:27:54','2025-06-20',1800.00),(7,1,101,2,'2025-04-24 11:47:32','2025-05-26',3100.00),(8,6,900,2,'2025-04-28 06:15:30','2025-04-29',5600.00),(9,6,800,4,'2025-04-28 06:36:24','2025-04-29',8400.00),(10,5,502,2,'2025-04-28 06:49:30','2025-04-29',2500.00),(11,1,501,3,'2025-04-29 04:16:27','2025-05-11',2400.00),(12,1,900,1,'2025-05-11 12:32:04','2025-09-29',2800.00);
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_agency`
--

DROP TABLE IF EXISTS `client_agency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client_agency` (
  `id_client` int NOT NULL AUTO_INCREMENT,
  `last_name` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(45) NOT NULL,
  `client_address` varchar(100) NOT NULL,
  `сlient_phonee` varchar(65) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`id_client`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_agency`
--

LOCK TABLES `client_agency` WRITE;
/*!40000 ALTER TABLE `client_agency` DISABLE KEYS */;
INSERT INTO `client_agency` VALUES (1,'Антипова','Поліна','Богданівна','м.Миколаїв, Соборна 8','+3806988965074','йцукен34'),(3,'Миколенко','Володимир','Сергійович','м.Суми, Першокласників 104','+380587345533','poiuy7'),(4,'Петренко','Василь','Павлович','м.Херсон, Куличів 3','+380976984677','ячсми2'),(5,'Петрушка','Іміль','Маркович','м.Миколаїв, вул.Водянична 3','+380936982266','длорпа4'),(6,'Криничков','Павло','Михайлович','м.Запоріжжя, Микитиного 91','+380976984678','фівап89'),(7,'Сопенко','Аліна','Петрівна','с.Зелений Гай','+380962296485','итьбю.79'),(14,'Антипова','Альбіна','Богданівна','с.Зелений Гай, Учительська 18','+380976984511','ячсм12'),(15,'Сергієнко','Максим','Васильович','м.Київ, Соборна 10','+380976980056','фіва3');
/*!40000 ALTER TABLE `client_agency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tours_travel`
--

DROP TABLE IF EXISTS `tours_travel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tours_travel` (
  `id_tours` int NOT NULL,
  `name_tours_tr` varchar(60) NOT NULL,
  `country` varchar(50) NOT NULL,
  `days_count` int NOT NULL,
  `price_per_person` decimal(6,2) NOT NULL,
  `description` varchar(400) NOT NULL,
  PRIMARY KEY (`id_tours`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tours_travel`
--

LOCK TABLES `tours_travel` WRITE;
/*!40000 ALTER TABLE `tours_travel` DISABLE KEYS */;
INSERT INTO `tours_travel` VALUES (100,'Романтична Італія','Італія',7,1200.00,'Тур для закоханих містами Італії.'),(101,'Скарби Північної Італії','Італія',10,1550.00,'Відкрийте для себе Мілан, Венецію, Флоренцію.'),(102,'Смаки Тоскани','Італія',6,1100.00,'Гастрономічний тур з дегустацією вин та сирів.'),(103,'Античний Рим та Ватикан','Італія',4,800.00,'	Екскурсія Колізеєм, Форумом та музеями Ватикану.'),(200,'Чарівний Париж','Франція',5,950.00,'Екскурсія основними пам\'ятками Парижа.'),(201,'Прованс та Лазурний Берег','Франція',8,1400.00,'Аромати лаванди та розкішні пляжі.'),(202,'Замки Луари','Франція',7,1350.00,'Відвідайте величні замки Шенонсо, Шамбор та інші.'),(203,'Ніцца та Французька Рив\'єра','Франція',9,1500.00,'Відпочинок на Лазурному березі.'),(300,'Берлінські канікули','Німеччина',4,700.00,'Історія та сучасність німецької столиці.'),(301,'Романтика Баварії','Німеччина',9,1300.00,'	Замки, озера та баварська гостинність.'),(302,'Романтичний Рейн','Німеччина',5,900.00,'Круїз по Рейну з оглядом замків та виноградників.'),(303,'Мюнхен та Баварські Альпи','Німеччина',6,1150.00,'Пивні сади, Альпійські краєвиди та замки.'),(400,'Іспанська Фієста','Іспанія',6,1050.00,'Барселона, Севілья, фламенко та сангрія.'),(401,'Мадрид - серце Іспанії','Іспанія',4,750.00,'Екскурсія музеєм Прадо, королівським палацом.'),(402,'Андалузьке Тріо: Севілья, Гранада, Кордова','Іспанія',8,1300.00,'Архітектура Альгамбри, фламенко та апельсинові сади.'),(500,'Грецькі Острови: Релакс Тур','Греція',12,1600.00,'Відпочинок на Санторіні та Міконосі.'),(501,'Афіни: Колиска Цивілізації','Греція',4,800.00,'	Огляд Акрополя, Парфенону та стародавньої Агори.'),(502,'Крит: Мінойська Цивілізація','Греція',7,1250.00,'	Палац Кносса, пляжі та критська кухня.'),(600,'Таємниці Старого Світу','Велика Британія',9,1800.00,'Лондон, Единбург, старовинні замки та легенди.'),(601,'Озерний Край Англії','Велика Британія',6,1100.00,'Мальовничі озера, піші прогулянки та поезія Вордсворта.'),(602,'Ірландські Легенди та Замки','Велика Британія',8,1450.00,'	Дублін, замки, скелі Мохер та ірландська музика.'),(701,'Каппадокія: Країна Куль','Туреччина	',5,950.00,'Повітряні кулі, печерні міста та неймовірні пейзажі.'),(702,'Анталія: Відпочинок на Узбережжі','Туреччина	',7,750.00,'Пляжі, античні руїни та середземноморська атмосфера.'),(800,'Природні Дива Норвегії	','Норвегія',10,2100.00,'	Фйорди, північне сяйво (взимку), мальовничі міста.'),(900,'Культурна Мозаїка Японії','Японія',14,2800.00,'Токіо, Кіото, традиції та сучасність.');
/*!40000 ALTER TABLE `tours_travel` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-16 13:25:51
