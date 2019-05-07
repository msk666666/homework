/*
 Navicat Premium Data Transfer

 Source Server         : host
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : imooc

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 07/05/2019 21:32:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint(20) NOT NULL COMMENT '商品的id',
  `goods_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品的名称',
  `goods_titile` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品的标题',
  `goods_img` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品的图片',
  `goods_detail` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '商品的详情',
  `goods_price` decimal(10, 2) DEFAULT NULL COMMENT '商品的价格',
  `goods_stock` int(11) DEFAULT NULL COMMENT '商品的库存',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, 'iphoneX', 'Apple iPhone X(A1865)', '/img/iphonex.png', 'Apple iPhoneX', 8765.00, 10);
INSERT INTO `goods` VALUES (2, 'HuaWei Nova', 'HuaWei Nova 3i 4+64G', '/img/meta10.png', 'Hua wei Nova 3i 6.5寸 黑', 2300.00, 10);

-- ----------------------------
-- Table structure for miaosha_goods
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_goods`;
CREATE TABLE `miaosha_goods`  (
  `id` bigint(20) NOT NULL COMMENT '秒杀商品的id',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '在商品表中的名字',
  `miaosha_price` decimal(10, 2) DEFAULT NULL COMMENT '秒杀的价格',
  `stock_count` int(11) DEFAULT NULL COMMENT '商品的库存',
  `start_date` datetime(0) DEFAULT NULL COMMENT '开始秒杀时间',
  `end_date` datetime(0) DEFAULT NULL COMMENT '结束秒杀时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of miaosha_goods
-- ----------------------------
INSERT INTO `miaosha_goods` VALUES (1, 1, 5000.00, 8, '2019-05-02 08:28:24', '2019-05-08 08:28:27');
INSERT INTO `miaosha_goods` VALUES (2, 2, 1000.00, 10, '2019-05-02 08:28:41', '2019-05-08 08:28:45');

-- ----------------------------
-- Table structure for miaosha_order
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_order`;
CREATE TABLE `miaosha_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `goods_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `u_uid_gid`(`user_id`, `goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1552 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of miaosha_order
-- ----------------------------
INSERT INTO `miaosha_order` VALUES (1550, 1, 1557, 1);
INSERT INTO `miaosha_order` VALUES (1551, 9, 1558, 1);

-- ----------------------------
-- Table structure for miaosha_user
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_user`;
CREATE TABLE `miaosha_user`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `salt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `head` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `register_date` datetime(0) DEFAULT NULL,
  `last_logindate` datetime(0) DEFAULT NULL,
  `login_count` int(11) DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of miaosha_user
-- ----------------------------
INSERT INTO `miaosha_user` VALUES (1, '13152089953', 'c5238144341f62c6afd57c0d08922a94', '1a2b3c4d', 'll', '2019-04-02 09:32:35', '2019-04-02 09:32:39', 1);
INSERT INTO `miaosha_user` VALUES (2, '15111819208', 'c5238144341f62c6afd57c0d08922a94', '1a2b3c4d', 'll', '2019-04-29 12:33:59', '2019-04-30 12:34:04', 0);
INSERT INTO `miaosha_user` VALUES (9, '18292953467', 'c5238144341f62c6afd57c0d08922a94', '1a2b3c4d', NULL, '2019-05-06 10:41:37', '2019-05-06 15:31:42', 7);

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `delivery_addr_id` bigint(20) DEFAULT NULL COMMENT '收货地址',
  `goods_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品名称',
  `goods_count` int(11) DEFAULT NULL COMMENT '商品数量',
  `goods_price` decimal(10, 2) DEFAULT NULL COMMENT '商品价格',
  `order_channel` tinyint(4) DEFAULT NULL COMMENT '订单渠道',
  `status` tinyint(4) DEFAULT NULL COMMENT '订单状态 0代表未支付 1代表已支付 2代表已发货 3代表已收货 4代表已退款 5代表已完成',
  `create_date` datetime(0) DEFAULT NULL COMMENT '下单时间',
  `pay_date` datetime(0) DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1559 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (1557, 1, 1, 1, 'iphoneX', 1, 5000.00, 1, 0, '2019-05-05 22:16:42', NULL);
INSERT INTO `order_info` VALUES (1558, 9, 1, 1, 'iphoneX', 1, 5000.00, 1, 0, '2019-05-06 12:33:57', NULL);

SET FOREIGN_KEY_CHECKS = 1;
