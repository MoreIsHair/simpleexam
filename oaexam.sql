/*
 Navicat Premium Data Transfer

 Source Server         : pengyy
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : 218.107.49.182:3306
 Source Schema         : oaexam

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 05/03/2020 15:42:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for system_auth_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_auth_menu`;
CREATE TABLE `system_auth_menu`  (
  `AuthId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MenuId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_authority
-- ----------------------------
DROP TABLE IF EXISTS `system_authority`;
CREATE TABLE `system_authority`  (
  `AuthId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ShowText` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ParentId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_departments
-- ----------------------------
DROP TABLE IF EXISTS `system_departments`;
CREATE TABLE `system_departments`  (
  `DeptId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DeptName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Description` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_group
-- ----------------------------
DROP TABLE IF EXISTS `system_group`;
CREATE TABLE `system_group`  (
  `GroupId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GroupName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DepartmentId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`  (
  `MenuId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ParentMenuId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MenuName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SortOrder` int(11) NULL DEFAULT NULL,
  `Path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `system_role_authority`;
CREATE TABLE `system_role_authority`  (
  `RoleId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `AuthId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu`  (
  `RoleId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MenuId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_roles
-- ----------------------------
DROP TABLE IF EXISTS `system_roles`;
CREATE TABLE `system_roles`  (
  `RoleId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RoleName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RoleType` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role`  (
  `UserId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RoleId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_users
-- ----------------------------
DROP TABLE IF EXISTS `system_users`;
CREATE TABLE `system_users`  (
  `UserId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DeptId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CreateTime` timestamp(0) NULL DEFAULT NULL,
  `LastEditTime` timestamp(0) NULL DEFAULT NULL,
  `Gender` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ProfilePicture` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Birthday` timestamp(0) NULL DEFAULT NULL,
  `RoleId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GroupId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Tel` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Type` int(1) NOT NULL COMMENT '表示用户的类型，0正式员工，1为临时性员工（外包）',
  `CompanyName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`UserId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_disposable_exam_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_disposable_exam_info`;
CREATE TABLE `tb_disposable_exam_info`  (
  `DisposableId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键而已，没有实际意义',
  `DisposableUsername` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `DisposableTel` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `DisposableCompany` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `ExamId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参加的考试ID',
  `Score` double(2, 2) NULL DEFAULT NULL COMMENT '考试得分',
  `SuspendTime` timestamp(0) NULL DEFAULT NULL COMMENT '考试暂停时间',
  `Surplus` double(2, 2) NULL DEFAULT NULL COMMENT '考试剩余时长',
  `StartExamTime` timestamp(0) NULL DEFAULT NULL COMMENT '开始考试时间',
  `FinishExamTime` timestamp(0) NULL DEFAULT NULL COMMENT '结束考试时间',
  `TemplateId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '面试考试模板ID',
  `ExamStatus` int(2) NULL DEFAULT NULL COMMENT '考试状态是否在考试时间内',
  PRIMARY KEY (`DisposableId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_exams
-- ----------------------------
DROP TABLE IF EXISTS `tb_exams`;
CREATE TABLE `tb_exams`  (
  `ExamId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ExamName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SubTitle` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CreateTime` timestamp(0) NULL DEFAULT NULL,
  `Creator` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LastEditTime` timestamp(0) NULL DEFAULT NULL,
  `LastEditor` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PublishTime` timestamp(0) NULL DEFAULT NULL,
  `Duration` int(11) NULL DEFAULT NULL,
  `DeadLine` timestamp(0) NULL DEFAULT NULL,
  `TeacherUserName` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ExamType` int(2) NOT NULL COMMENT '0内部考试，1外出培训考试'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_interview_question
-- ----------------------------
DROP TABLE IF EXISTS `tb_interview_question`;
CREATE TABLE `tb_interview_question`  (
  `InterviewQuestionId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `InterviewQuestionType` int(2) NULL DEFAULT NULL COMMENT '问题类型，选择，判断',
  `InterviewQuestionFollow` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问题属于什么分类，java，.net, 性格等等',
  `InterviewQuestionTitle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `InterviewQuestionText` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `InterviewQuestionScore` double(30, 2) NULL DEFAULT NULL,
  `CreateTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`InterviewQuestionId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_interview_template
-- ----------------------------
DROP TABLE IF EXISTS `tb_interview_template`;
CREATE TABLE `tb_interview_template`  (
  `InterviewTemplateId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板ID，即相当于一套可以重复的考试题ID',
  `Name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FollowType` int(6) NOT NULL COMMENT '模板类型，java，Net ，cad，性格',
  `Createtime` timestamp(0) NULL DEFAULT NULL,
  `Duration` int(4) NULL DEFAULT NULL COMMENT '考试规定时长',
  `CreateUsername` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建模板的用户名',
  PRIMARY KEY (`InterviewTemplateId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_interview_template_question
-- ----------------------------
DROP TABLE IF EXISTS `tb_interview_template_question`;
CREATE TABLE `tb_interview_template_question`  (
  `InterviewTemplateId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板Id',
  `InterviewQuestionId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '题库中问题Id',
  `OrderNum` int(3) NOT NULL COMMENT '题目在模板中的题号',
  PRIMARY KEY (`InterviewTemplateId`, `InterviewQuestionId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_option
-- ----------------------------
DROP TABLE IF EXISTS `tb_option`;
CREATE TABLE `tb_option`  (
  `OptionId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `OptionText` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `QuestionId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OrderNum` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`OptionId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_questions
-- ----------------------------
DROP TABLE IF EXISTS `tb_questions`;
CREATE TABLE `tb_questions`  (
  `QuestionId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `QuestionTitle` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `QuestionText` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ExamId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属于哪套试题',
  `OrderNum` int(11) NOT NULL,
  `QuestionType` int(11) NULL DEFAULT NULL COMMENT '问题类型，0单选-1多选题，1判断题，2简单题',
  `QuestionScore` decimal(30, 20) NULL DEFAULT NULL COMMENT '题目分数',
  PRIMARY KEY (`QuestionId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_reviews
-- ----------------------------
DROP TABLE IF EXISTS `tb_reviews`;
CREATE TABLE `tb_reviews`  (
  `ReviewId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `WellPrepared` int(11) NULL DEFAULT NULL,
  `Pronunciation` int(11) NULL DEFAULT NULL,
  `Experience` int(11) NULL DEFAULT NULL,
  `Logic` int(11) NULL DEFAULT NULL,
  `Understandable` int(11) NULL DEFAULT NULL,
  `HandoutQuality` int(11) NULL DEFAULT NULL,
  `Interaction` int(11) NULL DEFAULT NULL,
  `AnswerOnQuestion` int(11) NULL DEFAULT NULL,
  `Helpful` int(11) NULL DEFAULT NULL,
  `Sensible` int(11) NULL DEFAULT NULL,
  `TotalScore` int(11) NULL DEFAULT NULL,
  `Suggestion` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UserId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ExamId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DisposableUsername` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一次性扫码考试唯一用户名',
  PRIMARY KEY (`ReviewId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_answers
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_answers`;
CREATE TABLE `tb_user_answers`  (
  `UserId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OptionId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `AnswerValue` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `QuestionId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UserScore` decimal(30, 20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_exams
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_exams`;
CREATE TABLE `tb_user_exams`  (
  `UserId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ExamId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ExamStatus` int(11) NULL DEFAULT NULL,
  `Score` decimal(30, 20) NULL DEFAULT NULL,
  `FinishExamTime` timestamp(0) NULL DEFAULT NULL,
  `StartExamTime` varchar(27) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MarkTime` timestamp(0) NULL DEFAULT NULL,
  `SuspendTime` timestamp(0) NULL DEFAULT NULL,
  `Surplus` decimal(30, 20) NULL DEFAULT NULL,
  PRIMARY KEY (`UserId`, `ExamId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_valid_answers
-- ----------------------------
DROP TABLE IF EXISTS `tb_valid_answers`;
CREATE TABLE `tb_valid_answers`  (
  `QuestionId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OptionId` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `AnswerValue` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
