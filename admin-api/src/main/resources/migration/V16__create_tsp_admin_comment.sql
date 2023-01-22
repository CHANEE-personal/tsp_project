CREATE TABLE `tsp_admin_comment` (
                              `idx` int(11) NOT NULL AUTO_INCREMENT,
                              `comment` longtext NOT NULL,
                              `comment_type_idx` int(11) NOT NULL,
                              `comment_type` longtext NOT NULL,
                              `visible` varchar(255) DEFAULT 'N',
                              `create_time` datetime(6) DEFAULT NULL,
                              `creator` varchar(255) DEFAULT NULL,
                              `update_time` datetime(6) DEFAULT NULL,
                              `updater` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;