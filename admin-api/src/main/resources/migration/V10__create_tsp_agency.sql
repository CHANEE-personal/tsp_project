CREATE TABLE `tsp_agency` (
                                  `idx` int(11) NOT NULL AUTO_INCREMENT,
                                  `agency_name` varchar(255) NOT NULL,
                                  `agency_description` longtext DEFAULT NULL,
                                  `visible` varchar(255) DEFAULT NULL,
                                  `create_time` datetime(6) DEFAULT NULL,
                                  `creator` varchar(255) DEFAULT NULL,
                                  `update_time` datetime(6) DEFAULT NULL,
                                  `updater` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;