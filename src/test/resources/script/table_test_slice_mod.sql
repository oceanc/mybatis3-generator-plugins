ALTER TABLE `TABLE_TEST_SLICE_MOD`DROP PRIMARY KEY;

DROP TABLE `TABLE_TEST_SLICE_MOD`;

CREATE TABLE `TABLE_TEST_SLICE_MOD` (
`ID` bigint(20) NOT NULL AUTO_INCREMENT,
`SLICE_MOD_ID` bigint(32) NOT NULL DEFAULT 0,
`JACKSON_ID1` bigint(32) NOT NULL DEFAULT 0,
`JACKSON_ID2` varchar(255) NOT NULL,
`JACKSON_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`COULD_SUM_COL` int(8) NOT NULL DEFAULT 0,
`VERSION` bigint(32) NOT NULL DEFAULT 0,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1;
