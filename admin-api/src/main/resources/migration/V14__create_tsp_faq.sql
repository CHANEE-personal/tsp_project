CREATE TABLE `tsp_faq` (
                              `idx` int(11) NOT NULL AUTO_INCREMENT,
                              `title` varchar(255) NOT NULL,
                              `description` longtext NOT NULL,
                              `visible` varchar(255) DEFAULT 'N',
                              `view_count` int(11) DEFAULT 0,
                              `create_time` datetime(6) DEFAULT NULL,
                              `creator` varchar(255) DEFAULT NULL,
                              `update_time` datetime(6) DEFAULT NULL,
                              `updater` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;