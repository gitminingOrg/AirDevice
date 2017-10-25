DROP TABLE IF EXISTS `pre_binding`;
CREATE TABLE `pre_binding` (
  `bind_id` varchar(20) NOT NULL AUTO_INCREMENT,
  `uid` char(255) NOT NULL,
  `code_id` varchar(255) NOT NULL,
  `block_flag` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`bind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;