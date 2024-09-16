CREATE DATABASE `mp_demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

-- mp_demo.t_eqp definition

CREATE TABLE `t_eqp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `eqp_name` varchar(32) DEFAULT NULL,
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `owner_id` bigint DEFAULT NULL,
  `owner_name` varchar(32) DEFAULT NULL,
  `create_by` varchar(32) DEFAULT NULL,
  `create_time` datetime(3) DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime(3) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `deleted` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- mp_demo.t_user definition

CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `tel_number` varchar(11) DEFAULT NULL,
  `key_skills` varchar(255) DEFAULT NULL,
  `create_by` varchar(32) DEFAULT NULL,
  `create_time` datetime(3) DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime(3) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `deleted` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;