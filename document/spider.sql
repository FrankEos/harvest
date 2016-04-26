/*
Navicat MySQL Data Transfer

Source Server         : 61.160.200.224
Source Server Version : 50627
Source Host           : 61.160.200.224:3923
Source Database       : spider

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2015-12-01 22:14:50
*/

-- -----------------------------------------------------
-- use spider;
-- -----------------------------------------------------

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `harvest_sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `harvest_sys_menu`;
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

-- ----------------------------
-- Records of harvest_sys_menu
-- ----------------------------
INSERT INTO `harvest_sys_menu` VALUES ('1', '0', '系统管理', null, '1', '1', null);
INSERT INTO `harvest_sys_menu` VALUES ('2', '0', '网络爬虫', '', '0', '1', null);
INSERT INTO `harvest_sys_menu` VALUES ('3', '1', '菜单项管理', 'menu/list', '0', '1', null);
INSERT INTO `harvest_sys_menu` VALUES ('4', '2', '网站任务配置', 'portal/list', '0', '1', null);
INSERT INTO `harvest_sys_menu` VALUES ('5', '2', '处理器配置', 'processor/list', '1', '1', null);

-- ----------------------------
-- Table structure for `harvest_sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `harvest_sys_user`;
CREATE TABLE `harvest_sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of harvest_sys_user
-- ----------------------------
INSERT INTO `harvest_sys_user` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', '');


-- ----------------------------
-- Table structure for `harvest_portal`
-- ----------------------------
DROP TABLE IF EXISTS `harvest_portal`;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `harvest_processor`
-- ----------------------------
DROP TABLE IF EXISTS `harvest_processor`;
CREATE TABLE `harvest_processor` (
  `proc_class` varchar(125) NOT NULL,
  `proc_name` varchar(125) DEFAULT NULL,
  `proc_descr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`proc_class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `harvest_portal_processor`
-- ----------------------------
DROP TABLE IF EXISTS `harvest_portal_processor`;
CREATE TABLE `harvest_portal_processor` (
  `portal_id` int(11) NOT NULL,
  `regx_id` int(11) NOT NULL,
  `url_regx` varchar(255) DEFAULT NULL,
  `proc_class` varchar(128) DEFAULT NULL,
  `rule_file` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`portal_id`,`regx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
