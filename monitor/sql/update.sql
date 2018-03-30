create table `gmair_monitor`.`monitor_logo` (
`id` varchar(20) not null,
`code`  varchar(20) not null,
`path` varchar(20) not null,
`block_flag` tinyint(1) not null default 0,
`create_time` datetime not null,
primary key (`id`))
engine = InnoDB
