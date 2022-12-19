-- springboot_vue.sys_menu definition

CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(64) NOT NULL,
  `path` varchar(255) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(255) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `component` varchar(255) DEFAULT NULL,
  `type` int NOT NULL COMMENT '类型     0：目录   1：菜单   2：按钮',
  `icon` varchar(32) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int DEFAULT NULL COMMENT '排序',
  `created` datetime NOT NULL,
  `updated` datetime DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3;

INSERT INTO springboot_vue.sys_menu (parent_id,name,`path`,perms,component,`type`,icon,order_num,created,updated,status) VALUES
	 (0,'系统管理','','sys:manage','',0,'el-icon-s-operation',1,'2021-01-15 18:58:18','2021-01-15 18:58:20',1),
	 (1,'用户管理','/sys/users','sys:user:list','sys/User',1,'el-icon-s-custom',1,'2021-01-15 19:03:45','2021-01-15 19:03:48',1),
	 (1,'角色管理','/sys/roles','sys:role:list','sys/Role',1,'el-icon-rank',2,'2021-01-15 19:03:45','2021-01-15 19:03:48',1),
	 (1,'菜单管理','/sys/menus','sys:menu:list','sys/Menu',1,'el-icon-menu',3,'2021-01-15 19:03:45','2021-01-15 19:03:48',1),
	 (0,'系统工具','','sys:tools',NULL,0,'el-icon-s-tools',2,'2021-01-15 19:06:11',NULL,1),
	 (5,'数字字典','/sys/dicts','sys:dict:list','sys/Dict',1,'el-icon-s-order',1,'2021-01-15 19:07:18','2021-01-18 16:32:13',1),
	 (3,'添加角色','','sys:role:save','',2,'',1,'2021-01-15 23:02:25','2021-01-17 21:53:14',0),
	 (2,'添加用户',NULL,'sys:user:save',NULL,2,NULL,1,'2021-01-17 21:48:32',NULL,1),
	 (2,'修改用户',NULL,'sys:user:update',NULL,2,NULL,2,'2021-01-17 21:49:03','2021-01-17 21:53:04',1),
	 (2,'删除用户',NULL,'sys:user:delete',NULL,2,NULL,3,'2021-01-17 21:49:21',NULL,1);
INSERT INTO springboot_vue.sys_menu (parent_id,name,`path`,perms,component,`type`,icon,order_num,created,updated,status) VALUES
	 (2,'分配角色',NULL,'sys:user:role',NULL,2,NULL,4,'2021-01-17 21:49:58',NULL,1),
	 (2,'重置密码',NULL,'sys:user:repass',NULL,2,NULL,5,'2021-01-17 21:50:36',NULL,1),
	 (3,'修改角色',NULL,'sys:role:update',NULL,2,NULL,2,'2021-01-17 21:51:14',NULL,1),
	 (3,'删除角色',NULL,'sys:role:delete',NULL,2,NULL,3,'2021-01-17 21:51:39',NULL,1),
	 (3,'分配权限',NULL,'sys:role:perm',NULL,2,NULL,5,'2021-01-17 21:52:02',NULL,1),
	 (4,'添加菜单',NULL,'sys:menu:save',NULL,2,NULL,1,'2021-01-17 21:53:53','2021-01-17 21:55:28',1),
	 (4,'修改菜单',NULL,'sys:menu:update',NULL,2,NULL,2,'2021-01-17 21:56:12',NULL,1),
	 (4,'删除菜单',NULL,'sys:menu:delete',NULL,2,NULL,3,'2021-01-17 21:56:36',NULL,1);

-- springboot_vue.sys_role definition

CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `code` varchar(64) NOT NULL,
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE,
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

INSERT INTO springboot_vue.sys_role (name,code,remark,created,updated,status) VALUES
	 ('普通用户','normal','只有基本查看功能','2021-01-04 10:09:14','2021-01-30 08:19:52',1),
	 ('超级管理员','admin','系统默认最高权限，不可以编辑和任意修改','2021-01-16 13:29:03','2021-01-17 15:50:45',1);

-- springboot_vue.sys_role_menu definition

CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO springboot_vue.sys_role_menu (role_id,menu_id) VALUES
	 (6,1),
	 (6,2),
	 (6,9),
	 (6,10),
	 (6,11),
	 (6,12),
	 (6,13),
	 (6,3),
	 (6,7),
	 (6,14);
INSERT INTO springboot_vue.sys_role_menu (role_id,menu_id) VALUES
	 (6,15),
	 (6,16),
	 (6,4),
	 (6,17),
	 (6,18),
	 (6,19),
	 (6,5),
	 (6,6),
	 (3,1),
	 (3,2);
INSERT INTO springboot_vue.sys_role_menu (role_id,menu_id) VALUES
	 (3,3),
	 (3,4),
	 (3,5),
	 (3,6);

-- springboot_vue.sys_user definition

CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_USERNAME` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

INSERT INTO springboot_vue.sys_user (username,password,avatar,email,city,created,updated,last_login,status) VALUES
	 ('admin','$2a$10$RU29nWhKEKQgxVFbq6X8neiBpekPXphZmoYa7EN2xDZDvjNx10hsa','https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg','123@qq.com','广州','2021-01-12 22:13:53','2021-01-16 16:57:32','2020-12-30 08:38:37',1),
	 ('test','$2a$10$RU29nWhKEKQgxVFbq6X8neiBpekPXphZmoYa7EN2xDZDvjNx10hsa','https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg','test@qq.com','广州','2021-01-30 08:20:22','2021-01-30 08:55:57',NULL,1),
	 ('user','$2a$10$RU29nWhKEKQgxVFbq6X8neiBpekPXphZmoYa7EN2xDZDvjNx10hsa','https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg','user@qq.com','广州','2021-01-30 08:20:22','2021-01-30 08:55:57',NULL,1);

-- springboot_vue.sys_user_role definition

CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO springboot_vue.sys_user_role (user_id,role_id) VALUES
	 (1,6),
	 (1,3),
	 (2,3);
