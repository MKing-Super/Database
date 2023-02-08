建表：

```sql
CREATE [EXTERNAL] TABLE [IF NOT EXISTS] [database.]table_name
(column_definition1[, column_definition2, ...]
[, index_definition1[, ndex_definition12,]])
[ENGINE = [olap|mysql|elasticsearch|hive]]
[key_desc]
[COMMENT "table comment"];
[partition_desc]
[distribution_desc]
[rollup_index]
[PROPERTIES ("key"="value", ...)]
[BROKER PROPERTIES ("key"="value", ...)]
```







## 聚合模型

建表：

```sql
CREATE TABLE
IF NOT EXISTS test (
	site_id LARGEINT NOT NULL COMMENT "id of site",
	date DATE NOT NULL COMMENT "time of event",
	city_code VARCHAR (20) COMMENT "city_code of user",
	state INT REPLACE COMMENT "web state",
	pv BIGINT SUM DEFAULT "0" COMMENT "total page views"
) 
ENGINE=olap
AGGREGATE KEY (site_id, date, city_code) 
COMMENT "my first starrocks table"
DISTRIBUTED BY HASH (site_id) BUCKETS 8;
```

批量导入数据：

```sql
insert into test values(1,'2000-01-01','21',1,1);
insert into test values(1,'2000-01-01','21',1,1);
insert into test values(2,'2002-02-02','22',2,2);
insert into test values(2,'2002-02-02','22',2,2);
insert into test values(3,'2003-03-03','33',3,3);
insert into test values(3,'2003-03-03','33',3,3);
insert into test values(4,'2004-04-04','44',4,4);
```

删除数据：

```sql
DELETE FROM test where site_id in (1,2,3,4);
```





练习：

```sql
SELECT
	province,
	city,
	count(DISTINCT sid) count
FROM
	sys_view
WHERE
	created_time >= date_format(?,?)
AND created_time < date_format(?,?)
GROUP BY
	province,
	city;

----------------------------------------------------------------------

CREATE TABLE
IF NOT EXISTS slow_db.test (
	province VARCHAR (45) NOT NULL COMMENT "province",
	city VARCHAR (45) NOT NULL COMMENT "city",
	sid VARCHAR (36) COMMENT "member sid",
	sid_count INT SUM DEFAULT "0" COMMENT "count member sid",
	min_time DATE MIN COMMENT "min time",
	max_time DATE MAX COMMENT "max time"
) ENGINE = olap 
AGGREGATE KEY (province, city, sid) 
COMMENT "starrocks table" 
DISTRIBUTED BY HASH (province) BUCKETS 10;

-- ①
-- set enable_insert_strict = true;

INSERT INTO slow_db.test(province,city,sid,sid_count,min_time,max_time)
SELECT
	province,city,sid,1 AS sid_count,created_time AS min_time,created_time AS max_time
FROM slow_db.sys_view
WHERE 
created_time >= '2022-01-01'
AND created_time < '2022-01-05'
and province is not NULL 
and province != ''
and city is not null
and city != '';

select * from slow_db.test;
truncate table slow_db.test;



CREATE TABLE
IF NOT EXISTS slow_db.test0 (
	province VARCHAR (45) NOT NULL COMMENT "province",
	city VARCHAR (45) NOT NULL COMMENT "city",
	time_slot VARCHAR (30) NOT NULL COMMENT "time slot",
	pv_sum INT SUM COMMENT "count"
) ENGINE = olap 
AGGREGATE KEY (province, city,time_slot) 
COMMENT "starrocks table" 
DISTRIBUTED BY HASH (province) BUCKETS 10;


INSERT INTO slow_db.test0(province,city,time_slot,pv_sum)
SELECT province,city,"2022-01-01~2022-01-05" time_slot,"1" pv_sum
FROM slow_db.test
WHERE max_time >= '2022-01-01' AND max_time < '2022-01-05';

select * from slow_db.test0;
truncate table slow_db.test0;

```

[①]: https://docs.starrocks.io/zh-cn/latest/loading/InsertInto

