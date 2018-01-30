CREATE DATABASE  IF NOT EXISTS `nuo` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `nuo`;
-- MySQL dump 10.13  Distrib 5.6.11, for osx10.6 (i386)
--
-- Host: 47.100.24.40    Database: nuo
-- ------------------------------------------------------
-- Server version	5.7.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_class`
--

DROP TABLE IF EXISTS `t_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_class` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grade` tinyint(4) NOT NULL DEFAULT '0' COMMENT '年纪1-6',
  `number` tinyint(4) NOT NULL DEFAULT '0' COMMENT '班号01-99',
  `fk_school_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '归属学校',
  `fk_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  PRIMARY KEY (`pk_id`),
  KEY `idx_school_id` (`fk_school_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_class_stat`
--

DROP TABLE IF EXISTS `t_class_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_class_stat` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '批次中，班级老师点击状态完成时异步计算。',
  `subject` tinyint(4) NOT NULL DEFAULT '0' COMMENT '学科',
  `fk_score_batch_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '考试批次ID',
  `fk_school_id` bigint(20) NOT NULL DEFAULT '0',
  `fk_class_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `high_score` int(11) NOT NULL DEFAULT '0' COMMENT '最高分',
  `average_score` int(11) NOT NULL DEFAULT '0' COMMENT '平均分',
  `median_score` int(11) NOT NULL DEFAULT '0' COMMENT '中位数分数',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  PRIMARY KEY (`pk_id`),
  KEY `idx_score_batch_id` (`fk_score_batch_id`),
  KEY `idx_school_id` (`fk_school_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_course`
--

DROP TABLE IF EXISTS `t_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_course` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_class_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `pic` varchar(255) NOT NULL DEFAULT '' COMMENT '课程表图片地址',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00',
  `fk_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  PRIMARY KEY (`pk_id`),
  KEY `idx_class_id` (`fk_class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_exam`
--

DROP TABLE IF EXISTS `t_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_exam` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_school_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '学校ID',
  `grade` tinyint(4) NOT NULL DEFAULT '0' COMMENT '年级',
  `name` varchar(15) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '考试名称',
  `subjects` varchar(45) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '科目集合，逗号分隔',
  `fk_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态，1录入中，2统计中，3已统计',
  `fix_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '更新人',
  `fk_fix_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作者',
  PRIMARY KEY (`pk_id`),
  KEY `idx_school_id` (`fk_school_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_manager`
--

DROP TABLE IF EXISTS `t_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_manager` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(15) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '名称',
  `fk_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应用户ID',
  `fk_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间\n',
  PRIMARY KEY (`pk_id`),
  KEY `idx_user_id` (`fk_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_manager_class`
--

DROP TABLE IF EXISTS `t_manager_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_manager_class` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '管理ID，对应user表',
  `fk_school_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '学校ID',
  `fk_class_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应班级ID',
  `role` tinyint(4) NOT NULL DEFAULT '0' COMMENT '角色，1.超级管理员，2.学校管理员，3.班级管理员',
  `fk_create_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  PRIMARY KEY (`pk_id`),
  KEY `idx_school_id` (`fk_school_id`),
  KEY `idx_class_id` (`fk_class_id`),
  KEY `idx_manager_id` (`fk_manager_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_notice`
--

DROP TABLE IF EXISTS `t_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_notice` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '内容',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '类型，1：学校，2：班级',
  `fk_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '发布者',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '发布时间',
  `fk_school_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '归属学校ID',
  `fk_class_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '归属班级ID',
  PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_school`
--

DROP TABLE IF EXISTS `t_school`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_school` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(63) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '学校名称',
  `fk_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '类型，1小学，2初中，3高中',
  PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_score`
--

DROP TABLE IF EXISTS `t_score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_score` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1考试，2作业',
  `subject` tinyint(6) NOT NULL DEFAULT '0' COMMENT '科目，1语文，2数学，3英语，4地理，5政治，6物理，7化学，8生物，9文综，10理综',
  `fk_student_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '学生',
  `fk_class_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '成绩分数',
  `pic` varchar(255) NOT NULL DEFAULT '’‘' COMMENT '成绩照片地址',
  `fk_exam_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '考试ID',
  `fk_score_batch_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '该次考试归属哪一个批次',
  `fk_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  `school_rank` int(11) NOT NULL DEFAULT '0' COMMENT '学校排名',
  `class_rank` int(11) NOT NULL DEFAULT '0' COMMENT '班级排名',
  PRIMARY KEY (`pk_id`),
  KEY `idx_student_id` (`fk_student_id`),
  KEY `idx_score_batch_id` (`fk_score_batch_id`),
  KEY `idx_exam_id` (`fk_exam_id`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_score_batch`
--

DROP TABLE IF EXISTS `t_score_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_score_batch` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '类型 1考试，2作业',
  `fk_exam_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '考试ID',
  `subject` tinyint(6) NOT NULL DEFAULT '0' COMMENT '科目，1语文，2数学，3英语，4地理，5政治，6物理，7化学，8生物，9文综，10理综',
  `fk_school_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '学校ID',
  `fk_class_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `state` varchar(45) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '状态，1未完成，2等待更新统计，3已完成',
  `fk_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `create_time` varchar(45) COLLATE utf8_unicode_ci NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  `fk_fix_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作完成的管理员',
  `fix_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '完成时间',
  PRIMARY KEY (`pk_id`),
  KEY `idx_class_id` (`fk_class_id`),
  KEY `idx_exam_id` (`fk_exam_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_student`
--

DROP TABLE IF EXISTS `t_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_student` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(15) NOT NULL DEFAULT '' COMMENT '学生姓名',
  `birthday` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '学生生日',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别，1男，2女',
  `pic` varchar(255) NOT NULL DEFAULT '' COMMENT '头像地址',
  `fk_school_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '学校ID',
  `fk_class_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '班级id',
  `fk_manager_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  PRIMARY KEY (`pk_id`),
  KEY `idx_name` (`name`),
  KEY `idx_class_id` (`fk_class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_student_stat`
--

DROP TABLE IF EXISTS `t_student_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_student_stat` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '批次中，班级老师点击状态完成时异步计算。',
  `fk_student_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '学生ID',
  `subject` tinyint(4) NOT NULL DEFAULT '0' COMMENT '学科',
  `high_score` int(11) NOT NULL DEFAULT '0' COMMENT '最高分',
  `average_score` int(11) NOT NULL DEFAULT '0' COMMENT '平均分',
  `median_score` int(11) NOT NULL DEFAULT '0' COMMENT '中位数',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  PRIMARY KEY (`pk_id`),
  KEY `idx_student_id` (`fk_student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wechat_openid` varchar(45) NOT NULL DEFAULT '' COMMENT '微信唯一账号',
  `wechat_nickname` varchar(63) NOT NULL DEFAULT '' COMMENT '微信名',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别，1男，2女',
  `birthday` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '生日',
  `avatar_url` varchar(255) NOT NULL DEFAULT '' COMMENT '微信头像',
  `city` varchar(15) NOT NULL DEFAULT '' COMMENT '城市',
  `province` varchar(15) NOT NULL DEFAULT '' COMMENT '省份',
  `country` varchar(15) NOT NULL DEFAULT '' COMMENT '国家',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '上次登录时间',
  PRIMARY KEY (`pk_id`),
  KEY `idx_wechat_openid` (`wechat_openid`) COMMENT '微信openid索引',
  KEY `idx_wechat_nickname` (`wechat_nickname`) COMMENT '微信昵称索引'
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_student`
--

DROP TABLE IF EXISTS `t_user_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_student` (
  `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `fk_student_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '学生id',
  `fk_school_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '学校id',
  `fk_class_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '班级ID',
  `relationship` tinyint(4) NOT NULL DEFAULT '0' COMMENT '关系，1自己，2父亲，3母亲，4爷爷，5奶奶，6姑姑，7姑父，8姨，9姨父, 10创建者',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '创建时间',
  PRIMARY KEY (`pk_id`),
  KEY `idx_student_id` (`fk_student_id`),
  KEY `idx_user_id` (`fk_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-31  0:14:08
