CREATE TABLE `tsp_evaluate` (
                               `idx` int(11) NOT NULL AUTO_INCREMENT,
                               `support_idx` int(11) DEFAULT NULL,
                               `evaluate_comment` longtext DEFAULT NULL,
                               `visible` varchar(255) DEFAULT NULL,
                               `create_time` datetime(6) DEFAULT NULL,
                               `creator` varchar(255) DEFAULT NULL,
                               `update_time` datetime(6) DEFAULT NULL,
                               `updater` varchar(255) DEFAULT NULL,
                               PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;