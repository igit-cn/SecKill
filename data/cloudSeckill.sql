/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 50723
Source Host           : 119.23.19.240:3306
Source Database       : cloudSeckill

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-10-22 09:55:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel` (
  `id` int(15) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `wechat_id` varchar(100) NOT NULL DEFAULT '' COMMENT '微信ID',
  `channel_token` varchar(100) NOT NULL DEFAULT '' COMMENT '微信Token',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for email
-- ----------------------------
DROP TABLE IF EXISTS `email`;
CREATE TABLE `email` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `email_address` varchar(20) DEFAULT NULL,
  `random_code` int(4) DEFAULT NULL,
  `send_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `feedback_proxy_name` varchar(20) NOT NULL DEFAULT '',
  `content` varchar(200) NOT NULL DEFAULT '',
  `feedback_time` bigint(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for proxy
-- ----------------------------
DROP TABLE IF EXISTS `proxy`;
CREATE TABLE `proxy` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `proxy_name` varchar(50) NOT NULL,
  `password` varchar(32) NOT NULL,
  `child_exist` int(1) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '0',
  `previous_proxy_name` varchar(50) NOT NULL,
  `previous_proxy_id` int(6) NOT NULL,
  `proxy_level` int(4) NOT NULL DEFAULT '0',
  `register_time` bigint(15) NOT NULL DEFAULT '0',
  `email` varchar(20) NOT NULL,
  `remark` varchar(200) NOT NULL DEFAULT '',
  `parent_remark` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for proxy_list_mapper
-- ----------------------------
DROP TABLE IF EXISTS `proxy_list_mapper`;
CREATE TABLE `proxy_list_mapper` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `mapper_proxy_id` int(11) NOT NULL,
  `mapper_proxy_name` varchar(20) NOT NULL DEFAULT '',
  `child_exist` int(1) NOT NULL DEFAULT '0',
  `proxy_id` int(11) NOT NULL,
  `proxy_name` varchar(20) NOT NULL,
  `parent_proxy_id` int(11) NOT NULL,
  `parent_proxy_name` varchar(20) NOT NULL,
  `proxy_level` int(11) NOT NULL,
  `register_time` bigint(15) NOT NULL DEFAULT '0',
  `remark` varchar(200) DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `mapper_proxy_id` (`mapper_proxy_id`) USING BTREE,
  KEY `mapper_proxy_name` (`mapper_proxy_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for proxy_user
-- ----------------------------
DROP TABLE IF EXISTS `proxy_user`;
CREATE TABLE `proxy_user` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL,
  `use_time` bigint(50) NOT NULL,
  `status` int(1) unsigned zerofill DEFAULT '0',
  `register_time` bigint(15) NOT NULL,
  `remark` varchar(200) DEFAULT '',
  `password` varchar(32) NOT NULL,
  `email` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for recharge_code
-- ----------------------------
DROP TABLE IF EXISTS `recharge_code`;
CREATE TABLE `recharge_code` (
  `id` int(32) unsigned NOT NULL AUTO_INCREMENT,
  `recharge_code` varchar(20) DEFAULT NULL,
  `recharge_type` int(7) DEFAULT '0',
  `status` int(1) DEFAULT '0',
  `status_edit_level` int(5) DEFAULT NULL,
  `status_edit_id` int(5) DEFAULT NULL,
  `status_edit_name` varchar(20) DEFAULT NULL,
  `from_proxy_id` int(5) DEFAULT NULL,
  `from_proxy_name` varchar(20) DEFAULT NULL,
  `sell_status` int(1) DEFAULT '0',
  `sell_time` bigint(15) DEFAULT '0',
  `sell_remark` varchar(200) DEFAULT NULL,
  `recharge_user_id` int(5) DEFAULT NULL,
  `recharge_account` varchar(20) DEFAULT '',
  `recharge_status` int(1) DEFAULT '0',
  `recharge_time` bigint(15) DEFAULT '0',
  `create_time` bigint(15) DEFAULT '0',
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10073 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for recharge_code_transfer
-- ----------------------------
DROP TABLE IF EXISTS `recharge_code_transfer`;
CREATE TABLE `recharge_code_transfer` (
  `id` bigint(32) unsigned NOT NULL AUTO_INCREMENT,
  `recharge_code` varchar(20) NOT NULL,
  `recharge_code_type` int(8) NOT NULL,
  `from_proxy_name` varchar(50) NOT NULL DEFAULT '',
  `to_proxy_name` varchar(50) NOT NULL DEFAULT '',
  `transfer_time` bigint(15) NOT NULL DEFAULT '0',
  `remark` varchar(200) DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`recharge_code`,`from_proxy_name`,`to_proxy_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14823 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for recharge_code_type
-- ----------------------------
DROP TABLE IF EXISTS `recharge_code_type`;
CREATE TABLE `recharge_code_type` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `type` int(6) NOT NULL DEFAULT '100',
  `create_time` bigint(15) NOT NULL,
  `tips` varchar(10) NOT NULL,
  `use_time` bigint(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of recharge_code_type
-- ----------------------------
INSERT INTO `recharge_code_type` VALUES ('1', '1', '1507772253685', '天卡', '86400000');
INSERT INTO `recharge_code_type` VALUES ('2', '2', '1507772253685', '周卡', '604800000');
INSERT INTO `recharge_code_type` VALUES ('3', '3', '1507772253685', '月卡', '2678400000');
INSERT INTO `recharge_code_type` VALUES ('4', '4', '1507772253685', '季卡', '7948800000');
INSERT INTO `recharge_code_type` VALUES ('5', '5', '1507772253685', '年卡', '31622400000');

-- ----------------------------
-- Table structure for red_packet
-- ----------------------------
DROP TABLE IF EXISTS `red_packet`;
CREATE TABLE `red_packet` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `wechat_id` varchar(100) NOT NULL,
  `user_id` int(15) NOT NULL,
  `wechat_name` varchar(100) DEFAULT '' COMMENT '微信昵称',
  `money` int(10) NOT NULL,
  `packet_date` datetime NOT NULL,
  `group_name` varchar(100) DEFAULT NULL,
  `group_id` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7113 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `proxy_id` int(6) DEFAULT NULL,
  `proxy_name` varchar(20) DEFAULT NULL,
  `token` varchar(50) DEFAULT '',
  `create_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `proxy_id` (`proxy_id`),
  UNIQUE KEY `proxy_name` (`proxy_name`) USING BTREE,
  UNIQUE KEY `token` (`token`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1577 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(12) unsigned NOT NULL AUTO_INCREMENT,
  `from_user_name` varchar(50) NOT NULL DEFAULT '',
  `wechat_id` varchar(50) NOT NULL DEFAULT '',
  `user_id` varchar(50) NOT NULL DEFAULT '',
  `head_img` varchar(200) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `income` int(50) DEFAULT NULL,
  `expir_time` date DEFAULT NULL,
  `online_status` int(1) DEFAULT '0',
  `create_time` bigint(50) DEFAULT NULL,
  `pick_type` int(5) DEFAULT NULL,
  `pick_delay_time` bigint(50) DEFAULT NULL,
  `pick_delay` int(50) DEFAULT NULL,
  `pick_group_list_json` varchar(500) DEFAULT NULL,
  `auto_pick_personal` int(50) DEFAULT NULL,
  `auto_receive_transfer` int(50) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=197 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_coin_record
-- ----------------------------
DROP TABLE IF EXISTS `user_coin_record`;
CREATE TABLE `user_coin_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL DEFAULT '',
  `coin_count` int(10) unsigned zerofill NOT NULL,
  `date` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
