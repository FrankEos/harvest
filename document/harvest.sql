-- -----------------------------------------------------
-- --mysql -u root -p
-- --create database aguo;
-- --grant all privileges on aguo.* to frank@localhost identified by 'abc123';
-- --flush privileges;
-- --use aguo;
-- --http://61.160.200.224:9090/spider_aguo/
-- -----------------------------------------------------

-- -------------------------------
-- android apk
-- -------------------------------
DROP TABLE IF EXISTS `aguo_ecms_andriod`;
CREATE TABLE `aguo_ecms_andriod` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `ttid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `onclick` int(10) unsigned NOT NULL DEFAULT '0',
  `plnum` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `totaldown` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `newspath` char(20) NOT NULL DEFAULT '',
  `filename` char(36) NOT NULL DEFAULT '',
  `userid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `username` char(20) NOT NULL DEFAULT '',
  `firsttitle` tinyint(1) NOT NULL DEFAULT '0',
  `isgood` tinyint(2) NOT NULL DEFAULT '0',
  `ispic` tinyint(1) NOT NULL DEFAULT '0',
  `istop` tinyint(1) NOT NULL DEFAULT '0',
  `isqf` tinyint(1) NOT NULL DEFAULT '0',
  `ismember` tinyint(1) NOT NULL DEFAULT '0',
  `isurl` tinyint(1) NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  `groupid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `userfen` smallint(5) unsigned NOT NULL DEFAULT '0',
  `titlefont` char(14) NOT NULL DEFAULT '',
  `titleurl` char(200) NOT NULL DEFAULT '',
  `stb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `fstb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `restb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `keyboard` char(80) NOT NULL DEFAULT '',
  `title` char(100) NOT NULL DEFAULT '',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `titlepic` char(120) NOT NULL DEFAULT '',
  `version` char(50) NOT NULL DEFAULT '',
  `softsay` text NOT NULL,
  `downpath` mediumtext NOT NULL,
  `filesize` char(20) NOT NULL DEFAULT '',
  `system` char(100) NOT NULL DEFAULT '',
  `star` int(2) NOT NULL DEFAULT '0',
  `link` char(50) NOT NULL DEFAULT '',
  `morepic` mediumtext NOT NULL,
  `language` char(20) NOT NULL DEFAULT '',
  `title_version_md5` char(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `newstime` (`newstime`),
  KEY `ttid` (`ttid`),
  KEY `firsttitle` (`firsttitle`),
  KEY `isgood` (`isgood`),
  KEY `ispic` (`ispic`),
  KEY `idx_on_andriod1` (`link`),
  KEY `onclick` (`onclick`)
) ENGINE=MyISAM AUTO_INCREMENT=943720 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spider_img_md5`;
CREATE TABLE `spider_img_md5` (
  `file_md5` char(32) NOT NULL COMMENT '文件计算的MD5值',
  `file_path` varchar(128) NOT NULL COMMENT '相对路径以..开头',
  `file_abs_path` varchar(256) DEFAULT NULL COMMENT '绝对路径',
  `insert_time` int(11) DEFAULT NULL COMMENT '数据插入时间',
  PRIMARY KEY (`file_md5`,`file_path`),
  KEY `index_file_abs_path` (`file_abs_path`),
  KEY `index_insert_time` (`insert_time`),
  KEY `index_md5` (`file_md5`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='网站图片文件去重表';

DROP TABLE IF EXISTS `spider_apk_md5`;
CREATE TABLE `spider_apk_md5` (
  `file_md5` char(32) NOT NULL COMMENT '文件计算的MD5值',
  `file_path` varchar(128) NOT NULL COMMENT '相对路径以..开头',
  `file_abs_path` varchar(256) DEFAULT NULL COMMENT '绝对路径',
  `insert_time` int(11) DEFAULT NULL COMMENT '数据插入时间',
  PRIMARY KEY (`file_md5`,`file_path`),
  KEY `index_file_abs_path` (`file_abs_path`),
  KEY `index_insert_time` (`insert_time`),
  KEY `index_md5` (`file_md5`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='网站apk文件去重表';

DROP TABLE IF EXISTS `aguo_ecms_andriod_data_1`;
CREATE TABLE `aguo_ecms_andriod_data_1` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `keyid` char(255) NOT NULL DEFAULT '',
  `dokey` tinyint(1) NOT NULL DEFAULT '0',
  `newstempid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `closepl` tinyint(1) NOT NULL DEFAULT '0',
  `haveaddfen` tinyint(1) NOT NULL DEFAULT '0',
  `infotags` char(80) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `aguo_ecms_andriod_index`;
CREATE TABLE `aguo_ecms_andriod_index` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `checked` tinyint(1) NOT NULL DEFAULT '0',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `checked` (`checked`),
  KEY `newstime` (`newstime`),
  KEY `truetime` (`truetime`,`id`),
  KEY `havehtml` (`classid`,`truetime`,`havehtml`,`checked`,`id`)
) ENGINE=MyISAM AUTO_INCREMENT=944630 DEFAULT CHARSET=utf8;


-- --------------------------------
-- article
-- --------------------------------
DROP TABLE IF EXISTS `aguo_ecms_article`;
CREATE TABLE `aguo_ecms_article` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `ttid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `onclick` int(10) unsigned NOT NULL DEFAULT '0',
  `plnum` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `totaldown` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `newspath` char(20) NOT NULL DEFAULT '',
  `filename` char(36) NOT NULL DEFAULT '',
  `userid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `username` char(20) NOT NULL DEFAULT '',
  `firsttitle` tinyint(1) NOT NULL DEFAULT '0',
  `isgood` tinyint(1) NOT NULL DEFAULT '0',
  `ispic` tinyint(1) NOT NULL DEFAULT '0',
  `istop` tinyint(1) NOT NULL DEFAULT '0',
  `isqf` tinyint(1) NOT NULL DEFAULT '0',
  `ismember` tinyint(1) NOT NULL DEFAULT '0',
  `isurl` tinyint(1) NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  `groupid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `userfen` smallint(5) unsigned NOT NULL DEFAULT '0',
  `titlefont` char(14) NOT NULL DEFAULT '',
  `titleurl` char(200) NOT NULL DEFAULT '',
  `stb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `fstb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `restb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `keyboard` char(80) NOT NULL DEFAULT '',
  `title` char(100) NOT NULL DEFAULT '',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `titlepic` char(120) NOT NULL DEFAULT '',
  `ftitle` char(120) NOT NULL DEFAULT '',
  `smalltext` mediumtext NOT NULL,
  `writer` char(30) NOT NULL DEFAULT '',
  `befrom` char(60) NOT NULL DEFAULT '',
  `newstext` char(50) NOT NULL DEFAULT '',
  `diggtop` int(11) NOT NULL DEFAULT '0',
  `game` char(20) NOT NULL DEFAULT '',
  `smalltext_md5` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `newstime` (`newstime`),
  KEY `ttid` (`ttid`),
  KEY `firsttitle` (`firsttitle`),
  KEY `isgood` (`isgood`),
  KEY `ispic` (`ispic`),
  KEY `idx_on_arcticle1` (`game`),
  KEY `onclick` (`onclick`),
  KEY `index_smalltext_md5` (`smalltext_md5`)
) ENGINE=MyISAM AUTO_INCREMENT=551548 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `aguo_ecms_article_index`;
CREATE TABLE `aguo_ecms_article_index` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `checked` tinyint(1) NOT NULL DEFAULT '0',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `checked` (`checked`),
  KEY `newstime` (`newstime`),
  KEY `truetime` (`truetime`,`id`),
  KEY `havehtml` (`classid`,`truetime`,`havehtml`,`checked`,`id`)
) ENGINE=MyISAM AUTO_INCREMENT=551548 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `aguo_ecms_article_data_1`;
CREATE TABLE `aguo_ecms_article_data_1` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `keyid` char(255) NOT NULL DEFAULT '',
  `dokey` tinyint(1) NOT NULL DEFAULT '0',
  `newstempid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `closepl` tinyint(1) NOT NULL DEFAULT '0',
  `haveaddfen` tinyint(1) NOT NULL DEFAULT '0',
  `infotags` char(80) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------
-- iPhone app
-- --------------------------------
DROP TABLE IF EXISTS `aguo_ecms_iphone`;
CREATE TABLE `aguo_ecms_iphone` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `ttid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `onclick` int(10) unsigned NOT NULL DEFAULT '0',
  `plnum` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `totaldown` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `newspath` char(20) NOT NULL DEFAULT '',
  `filename` char(36) NOT NULL DEFAULT '',
  `userid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `username` char(20) NOT NULL DEFAULT '',
  `firsttitle` tinyint(1) NOT NULL DEFAULT '0',
  `isgood` tinyint(2) NOT NULL DEFAULT '0',
  `ispic` tinyint(1) NOT NULL DEFAULT '0',
  `istop` tinyint(1) NOT NULL DEFAULT '0',
  `isqf` tinyint(1) NOT NULL DEFAULT '0',
  `ismember` tinyint(1) NOT NULL DEFAULT '0',
  `isurl` tinyint(1) NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  `groupid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `userfen` smallint(5) unsigned NOT NULL DEFAULT '0',
  `titlefont` char(14) NOT NULL DEFAULT '',
  `titleurl` char(200) NOT NULL DEFAULT '',
  `stb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `fstb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `restb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `keyboard` char(80) NOT NULL DEFAULT '',
  `title` char(100) NOT NULL DEFAULT '',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `titlepic` char(120) NOT NULL DEFAULT '',
  `version` char(50) NOT NULL DEFAULT '',
  `softsay` text NOT NULL,
  `downpath` mediumtext NOT NULL,
  `filesize` char(20) NOT NULL DEFAULT '',
  `system` char(100) NOT NULL DEFAULT '',
  `star` int(2) NOT NULL DEFAULT '0',
  `link` char(50) NOT NULL DEFAULT '',
  `morepic` mediumtext NOT NULL,
  `language` char(20) NOT NULL DEFAULT '',
  `ipadown` mediumtext NOT NULL,
  `title_version_md5` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `newstime` (`newstime`),
  KEY `ttid` (`ttid`),
  KEY `firsttitle` (`firsttitle`),
  KEY `isgood` (`isgood`),
  KEY `ispic` (`ispic`),
  KEY `idx_on_iphone1` (`link`),
  KEY `onclick` (`onclick`),
  KEY `index_title_version_md5` (`title_version_md5`)
) ENGINE=MyISAM AUTO_INCREMENT=179792 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spider_ipa_md5`;
CREATE TABLE `spider_ipa_md5` (
  `file_md5` char(32) NOT NULL COMMENT '文件计算的MD5值',
  `file_path` varchar(128) NOT NULL COMMENT '相对路径以..开头',
  `file_abs_path` varchar(256) DEFAULT NULL COMMENT '绝对路径',
  `insert_time` int(11) DEFAULT NULL COMMENT '数据插入时间',
  PRIMARY KEY (`file_md5`,`file_path`),
  KEY `index_file_abs_path` (`file_abs_path`),
  KEY `index_insert_time` (`insert_time`),
  KEY `index_md5` (`file_md5`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='网站ipa文件去重表';

DROP TABLE IF EXISTS `aguo_ecms_iphone_index`;
CREATE TABLE `aguo_ecms_iphone_index` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `checked` tinyint(1) NOT NULL DEFAULT '0',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `checked` (`checked`),
  KEY `newstime` (`newstime`),
  KEY `truetime` (`truetime`,`id`),
  KEY `havehtml` (`classid`,`truetime`,`havehtml`,`checked`,`id`)
) ENGINE=MyISAM AUTO_INCREMENT=179792 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `aguo_ecms_iphone_data_1`;
CREATE TABLE `aguo_ecms_iphone_data_1` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `keyid` char(255) NOT NULL DEFAULT '',
  `dokey` tinyint(1) NOT NULL DEFAULT '0',
  `newstempid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `closepl` tinyint(1) NOT NULL DEFAULT '0',
  `haveaddfen` tinyint(1) NOT NULL DEFAULT '0',
  `infotags` char(80) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------
-- iPad app
-- --------------------------------
DROP TABLE IF EXISTS `aguo_ecms_ipad`;
 CREATE TABLE `aguo_ecms_ipad` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `ttid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `onclick` int(10) unsigned NOT NULL DEFAULT '0',
  `plnum` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `totaldown` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `newspath` char(20) NOT NULL DEFAULT '',
  `filename` char(36) NOT NULL DEFAULT '',
  `userid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `username` char(20) NOT NULL DEFAULT '',
  `firsttitle` tinyint(1) NOT NULL DEFAULT '0',
  `isgood` tinyint(2) NOT NULL DEFAULT '0',
  `ispic` tinyint(1) NOT NULL DEFAULT '0',
  `istop` tinyint(1) NOT NULL DEFAULT '0',
  `isqf` tinyint(1) NOT NULL DEFAULT '0',
  `ismember` tinyint(1) NOT NULL DEFAULT '0',
  `isurl` tinyint(1) NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  `groupid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `userfen` smallint(5) unsigned NOT NULL DEFAULT '0',
  `titlefont` char(14) NOT NULL DEFAULT '',
  `titleurl` char(200) NOT NULL DEFAULT '',
  `stb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `fstb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `restb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `keyboard` char(80) NOT NULL DEFAULT '',
  `title` char(100) NOT NULL DEFAULT '',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `titlepic` char(120) NOT NULL DEFAULT '',
  `version` char(50) NOT NULL DEFAULT '',
  `softsay` text NOT NULL,
  `downpath` mediumtext NOT NULL,
  `filesize` char(20) NOT NULL DEFAULT '',
  `system` char(100) NOT NULL DEFAULT '',
  `star` int(2) NOT NULL DEFAULT '0',
  `link` char(50) NOT NULL DEFAULT '',
  `morepic` mediumtext NOT NULL,
  `language` char(20) NOT NULL DEFAULT '',
  `ipadown` mediumtext NOT NULL,
  `title_version_md5` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `newstime` (`newstime`),
  KEY `ttid` (`ttid`),
  KEY `firsttitle` (`firsttitle`),
  KEY `isgood` (`isgood`),
  KEY `ispic` (`ispic`),
  KEY `idx_on_ipad1` (`link`),
  KEY `onclick` (`onclick`),
  KEY `index_title_version_md5` (`title_version_md5`)
) ENGINE=MyISAM AUTO_INCREMENT=100308 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `aguo_ecms_ipad_index`;
 CREATE TABLE `aguo_ecms_ipad_index` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `checked` tinyint(1) NOT NULL DEFAULT '0',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `checked` (`checked`),
  KEY `newstime` (`newstime`),
  KEY `truetime` (`truetime`,`id`),
  KEY `havehtml` (`classid`,`truetime`,`havehtml`,`checked`,`id`)
) ENGINE=MyISAM AUTO_INCREMENT=100308 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `aguo_ecms_ipad_data_1`;
CREATE TABLE `aguo_ecms_ipad_data_1` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `keyid` char(255) NOT NULL DEFAULT '',
  `dokey` tinyint(1) NOT NULL DEFAULT '0',
  `newstempid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `closepl` tinyint(1) NOT NULL DEFAULT '0',
  `haveaddfen` tinyint(1) NOT NULL DEFAULT '0',
  `infotags` char(80) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------
-- Edu
-- --------------------------------
DROP TABLE IF EXISTS `aguo_ecms_edu`;
CREATE TABLE `aguo_ecms_edu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `ttid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `onclick` int(10) unsigned NOT NULL DEFAULT '0',
  `plnum` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `totaldown` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `newspath` char(20) NOT NULL DEFAULT '',
  `filename` char(36) NOT NULL DEFAULT '',
  `userid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `username` char(20) NOT NULL DEFAULT '',
  `firsttitle` tinyint(1) NOT NULL DEFAULT '0',
  `isgood` tinyint(1) NOT NULL DEFAULT '0',
  `ispic` tinyint(1) NOT NULL DEFAULT '0',
  `istop` tinyint(1) NOT NULL DEFAULT '0',
  `isqf` tinyint(1) NOT NULL DEFAULT '0',
  `ismember` tinyint(1) NOT NULL DEFAULT '0',
  `isurl` tinyint(1) NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  `groupid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `userfen` smallint(5) unsigned NOT NULL DEFAULT '0',
  `titlefont` char(14) NOT NULL DEFAULT '',
  `titleurl` char(200) NOT NULL DEFAULT '',
  `stb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `fstb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `restb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `keyboard` char(80) NOT NULL DEFAULT '',
  `title` char(100) NOT NULL DEFAULT '',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `titlepic` char(120) NOT NULL DEFAULT '',
  `type` varchar(100) NOT NULL DEFAULT '',
  `smalltext` mediumtext NOT NULL,
  `smalltext_md5` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `newstime` (`newstime`),
  KEY `ttid` (`ttid`),
  KEY `firsttitle` (`firsttitle`),
  KEY `isgood` (`isgood`),
  KEY `ispic` (`ispic`),
  KEY `index_smalltext_md5` (`smalltext_md5`)
) ENGINE=MyISAM AUTO_INCREMENT=60868 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `aguo_ecms_edu_index`;
CREATE TABLE `aguo_ecms_edu_index` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `checked` tinyint(1) NOT NULL DEFAULT '0',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `checked` (`checked`),
  KEY `newstime` (`newstime`),
  KEY `truetime` (`truetime`,`id`),
  KEY `havehtml` (`classid`,`truetime`,`havehtml`,`checked`,`id`)
) ENGINE=MyISAM AUTO_INCREMENT=60868 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `aguo_ecms_edu_data_1`;
 CREATE TABLE `aguo_ecms_edu_data_1` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `keyid` char(255) NOT NULL DEFAULT '',
  `dokey` tinyint(1) NOT NULL DEFAULT '0',
  `newstempid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `closepl` tinyint(1) NOT NULL DEFAULT '0',
  `haveaddfen` tinyint(1) NOT NULL DEFAULT '0',
  `infotags` char(80) NOT NULL DEFAULT '',
  `writer` varchar(100) NOT NULL DEFAULT '',
  `newstext` mediumtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `aguo_ecms_wallpaper`
--

CREATE TABLE `aguo_ecms_wallpaper` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `ttid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `onclick` int(10) unsigned NOT NULL DEFAULT '0',
  `plnum` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `totaldown` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `newspath` char(20) NOT NULL DEFAULT '',
  `filename` char(36) NOT NULL DEFAULT '',
  `userid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `username` char(20) NOT NULL DEFAULT '',
  `firsttitle` tinyint(1) NOT NULL DEFAULT '0',
  `isgood` tinyint(1) NOT NULL DEFAULT '0',
  `ispic` tinyint(1) NOT NULL DEFAULT '0',
  `istop` tinyint(1) NOT NULL DEFAULT '0',
  `isqf` tinyint(1) NOT NULL DEFAULT '0',
  `ismember` tinyint(1) NOT NULL DEFAULT '0',
  `isurl` tinyint(1) NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  `groupid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `userfen` smallint(5) unsigned NOT NULL DEFAULT '0',
  `titlefont` char(14) NOT NULL DEFAULT '',
  `titleurl` char(200) NOT NULL DEFAULT '',
  `stb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `fstb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `restb` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `keyboard` char(80) NOT NULL DEFAULT '',
  `title` char(100) NOT NULL DEFAULT '',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `titlepic` char(120) NOT NULL DEFAULT '',
  `size` char(16) NOT NULL DEFAULT '',
  `type` char(20) NOT NULL DEFAULT '',
  `newstext` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `newstime` (`newstime`),
  KEY `ttid` (`ttid`),
  KEY `firsttitle` (`firsttitle`),
  KEY `isgood` (`isgood`),
  KEY `ispic` (`ispic`),
  KEY `onclick` (`onclick`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=265 ;

-- --------------------------------------------------------

--
-- 表的结构 `aguo_ecms_wallpaper_data_1`
--

CREATE TABLE `aguo_ecms_wallpaper_data_1` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `keyid` char(255) NOT NULL DEFAULT '',
  `dokey` tinyint(1) NOT NULL DEFAULT '0',
  `newstempid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `closepl` tinyint(1) NOT NULL DEFAULT '0',
  `haveaddfen` tinyint(1) NOT NULL DEFAULT '0',
  `infotags` char(80) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `aguo_ecms_wallpaper_index`
--

CREATE TABLE `aguo_ecms_wallpaper_index` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `checked` tinyint(1) NOT NULL DEFAULT '0',
  `newstime` int(10) unsigned NOT NULL DEFAULT '0',
  `truetime` int(10) unsigned NOT NULL DEFAULT '0',
  `lastdotime` int(10) unsigned NOT NULL DEFAULT '0',
  `havehtml` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `classid` (`classid`),
  KEY `checked` (`checked`),
  KEY `newstime` (`newstime`),
  KEY `truetime` (`truetime`,`id`),
  KEY `havehtml` (`classid`,`truetime`,`havehtml`,`checked`,`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=320 ;
