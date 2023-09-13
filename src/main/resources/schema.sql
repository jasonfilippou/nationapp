USE nations;

--
-- Table structure for table `continents`
--

DROP TABLE IF EXISTS `continents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `continents` (
                              `continent_id` int(11) NOT NULL AUTO_INCREMENT,
                              `name` varchar(255) NOT NULL,
                              PRIMARY KEY (`continent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `languages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `languages` (
                             `language_id` int(11) NOT NULL AUTO_INCREMENT,
                             `language` varchar(50) NOT NULL,
                             PRIMARY KEY (`language_id`)
) ENGINE=InnoDB AUTO_INCREMENT=458 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `regions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `regions` (
                           `region_id` int(11) NOT NULL AUTO_INCREMENT,
                           `name` varchar(100) NOT NULL,
                           `continent_id` int(11) NOT NULL,
                           PRIMARY KEY (`region_id`),
                           KEY `continent_id` (`continent_id`),
                           CONSTRAINT `regions_ibfk_1` FOREIGN KEY (`continent_id`) REFERENCES `continents` (`continent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `countries` (
                             `country_id` int(11) NOT NULL AUTO_INCREMENT,
                             `name` varchar(50) DEFAULT NULL,
                             `area` decimal(10,2) NOT NULL,
                             `national_day` date DEFAULT NULL,
                             `country_code2` char(2) NOT NULL,
                             `country_code3` char(3) NOT NULL,
                             `region_id` int(11) NOT NULL,
                             PRIMARY KEY (`country_id`),
                             UNIQUE KEY `country_code2` (`country_code2`),
                             UNIQUE KEY `country_code3` (`country_code3`),
                             KEY `region_id` (`region_id`),
                             CONSTRAINT `countries_ibfk_1` FOREIGN KEY (`region_id`) REFERENCES `regions` (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=240 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `country_languages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `country_languages` (
                                     `country_id` int(11) NOT NULL,
                                     `language_id` int(11) NOT NULL,
                                     `official` tinyint(1) NOT NULL,
                                     PRIMARY KEY (`country_id`,`language_id`),
                                     KEY `language_id` (`language_id`),
                                     CONSTRAINT `country_languages_ibfk_1` FOREIGN KEY (`country_id`) REFERENCES `countries` (`country_id`),
                                     CONSTRAINT `country_languages_ibfk_2` FOREIGN KEY (`language_id`) REFERENCES `languages` (`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `country_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `country_stats` (
                                 `country_id` int(11) NOT NULL,
                                 `year` int(11) NOT NULL,
                                 `population` int(11) DEFAULT NULL,
                                 `gdp` decimal(15,0) DEFAULT NULL,
                                 PRIMARY KEY (`country_id`,`year`),
                                 CONSTRAINT `country_stats_ibfk_1` FOREIGN KEY (`country_id`) REFERENCES `countries` (`country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `guests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `guests` (
                          `guest_id` int(11) NOT NULL,
                          `name` varchar(100) NOT NULL,
                          PRIMARY KEY (`guest_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `region_areas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `region_areas` (
                                `region_name` varchar(100) NOT NULL,
                                `region_area` decimal(15,2) NOT NULL,
                                PRIMARY KEY (`region_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `vips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `vips` (
                        `vip_id` int(11) NOT NULL,
                        `name` varchar(100) NOT NULL,
                        PRIMARY KEY (`vip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;