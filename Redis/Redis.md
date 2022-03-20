# Redis学习笔记

​	redis是一种高级的key:value存储系统，其中value支持五种数据类型：

1.字符串（strings）
 2.字符串列表（lists）
 3.字符串集合（sets）
 4.有序字符串集合（sorted sets）
 5.哈希（hashes）

## 1 数据类型

### 1.1 字符串

```csharp
set str "hello"
get str
    
    
set num 2
get num	     // -> 2
incr num     // -> 3
get num      // -> 3
```

incr -> 每执行一次就+1，原子计数。

### 1.2.字符串列表

​	list底层是链表。在头部和尾部插入元素快，元素定位慢。

```csharp
lpush list mk		//左边插入
rpush list 666		//右边插入
lpush list emmm
lrange list 0 1     //查从0~1的元素
lrange list 0 -1	//查从0~倒数第一个
```



### 1.3.字符串集合

​	set无序集合。

```csharp
sadd set one		//添加
sadd set mk
sadd set ppp
smembers set				//查全部
sismember set ttt		//查询集合中有没有“ttt”
sismember set one

sadd set1 666
sadd set1 ppp
smembers set

sinter set set1			//交集
sunion set set1			//并集
sdiff set set1			//差集
```



### 1.4.有序字符串集合

​	sorted sets有序集合。

```csharp
zadd zset 1 emm				//添加 序号 + value
zadd zset 2 mk
zadd zset 2 999
zrange zset 0 -1			//查全部
zrange zset 0 -1 withscores	//查全部和序号
```



### 1.5.哈希

​	hash。

```csharp
//存入的数据：name->mk;pw->66;sex->man
hmset mm:001 name mk pw 666 sex man
hgetall mm:001
hset mm:001 pw 123456		//用过key修改value
```







