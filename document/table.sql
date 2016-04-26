CREATE TABLE `harvest_content` (
  `url_md5` varchar(32) NOT NULL,
  `url` varchar(512) DEFAULT NULL,
  `url_type` smallint(6) DEFAULT '1',
  `cost_time` int(11) DEFAULT NULL,
  `oper_flag` smallint(6) DEFAULT NULL,
  `content_md5` varchar(32) DEFAULT '',
  `update_time` bigint(20) DEFAULT NULL,
  `tag` varchar(32) NOT NULL,
  `order` bigint(32) DEFAULT NULL,
  `bak` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`url_md5`,`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `harvest_portal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `portal_name` varchar(64) DEFAULT NULL,
  `portal_desc` varchar(255) DEFAULT NULL,
  `isEPG` smallint(6) DEFAULT '0',
  `max_thread` smallint(6) DEFAULT NULL,
  `cycle` smallint(6) DEFAULT NULL,
  `show_flag` smallint(6) DEFAULT NULL,
  `isAllByHand` smallint(6) DEFAULT '0',
  `incrementPageCount` smallint(6) DEFAULT '5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

CREATE TABLE `harvest_portal_processor` (
  `portal_id` int(11) NOT NULL,
  `regx_id` int(11) NOT NULL,
  `url_regx` varchar(255) DEFAULT NULL,
  `proc_class` varchar(128) DEFAULT NULL,
  `rule_file` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`portal_id`,`regx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `harvest_processor` (
  `proc_class` varchar(125) NOT NULL,
  `proc_name` varchar(125) DEFAULT NULL,
  `proc_descr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`proc_class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `harvest_sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `link` varchar(100) DEFAULT NULL,
  `order_by` smallint(6) DEFAULT NULL,
  `status` smallint(6) DEFAULT '0' COMMENT '0 closed/1 open',
  `show_flag` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;


CREATE TABLE `harvest_sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

