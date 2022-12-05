









## 聚合模型

建表：

```sql
CREATE TABLE
IF NOT EXISTS test (
	site_id LARGEINT NOT NULL COMMENT "id of site",
	date DATE NOT NULL COMMENT "time of event",
	city_code VARCHAR (20) COMMENT "city_code of user",
	state INT COMMENT "web state",
	pv BIGINT SUM DEFAULT "0" COMMENT "total page views"
) DISTRIBUTED BY HASH (site_id) BUCKETS 8;
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

