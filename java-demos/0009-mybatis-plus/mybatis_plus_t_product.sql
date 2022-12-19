create table t_product
(
	id bigint(20) not null comment '主键id',
	name varchar(30) null default null comment '商品名称',
	price int(11) default 0 comment '价格',
	version int(11) default 0 comment '乐观锁版本号',
	primary key (id)
)