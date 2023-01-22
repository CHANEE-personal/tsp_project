CREATE TABLE `tsp_model_schedule` (
                                     `idx` int(11) NOT NULL AUTO_INCREMENT,
                                     `model_idx` int(11) NOT NULL,
                                     `model_schedule` longtext NOT NULL,
                                     `model_schedule_time` datetime(6) NOT NULL,
                                     `visible` varchar(255) DEFAULT 'N',
                                     `create_time` datetime(6) DEFAULT NULL,
                                     `creator` varchar(255) DEFAULT NULL,
                                     `update_time` datetime(6) DEFAULT NULL,
                                     `updater` varchar(255) DEFAULT NULL,
                                     PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;