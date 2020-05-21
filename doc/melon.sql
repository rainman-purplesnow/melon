/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.100
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : 192.168.1.100:3306
 Source Schema         : melon

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 12/10/2019 17:34:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gray_config
-- ----------------------------
DROP TABLE IF EXISTS `gray_config`;
CREATE TABLE `gray_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `melon_id` bigint(20) NOT NULL,
  `gray_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rule` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `groups` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `condition_json` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `condition_class` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT 1,
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gray_config
-- ----------------------------
INSERT INTO `gray_config` VALUES (1, 1, 'g-1', 'zuul-path-gray-rule', 'b', '{\"method\":\"GET\",\"uri\":\"/demo-a/test\"}', 'org.tieland.melon.core.PathGrayCondition', 1, '2019-08-28 18:31:11', '2019-08-29 20:19:45');
INSERT INTO `gray_config` VALUES (2, 2, 'g-2', 'zuul-path-gray-rule', 'b', '{\"method\":\"GET\",\"uri\":\"/demo-a/test1\"}', 'org.tieland.melon.core.PathGrayCondition', 1, '2019-08-29 15:53:58', '2019-08-29 20:17:17');

-- ----------------------------
-- Table structure for melon_config
-- ----------------------------
DROP TABLE IF EXISTS `melon_config`;
CREATE TABLE `melon_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `primary_groups` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `forbidden_groups` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` int(2) NOT NULL,
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of melon_config
-- ----------------------------
INSERT INTO `melon_config` VALUES (1, 'NORMAL', '', '', 1, '2019-08-28 18:12:33', '2019-08-29 20:22:09');

SET FOREIGN_KEY_CHECKS = 1;
