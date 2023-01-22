CREATE TABLE `tsp_festival` (
                                   `idx` int(11) NOT NULL AUTO_INCREMENT,
                                   `festival_title` longtext NOT NULL,
                                   `festival_description` longtext NOT NULL,
                                   `festival_month` int(11) NOT NULL,
                                   `festival_day` int(11) NOT NULL,
                                   `festival_time` datetime(6) NOT NULL,
                                   `create_time` datetime(6) DEFAULT NULL,
                                   `creator` varchar(255) DEFAULT NULL,
                                   `update_time` datetime(6) DEFAULT NULL,
                                   `updater` varchar(255) DEFAULT NULL,
                                   PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;