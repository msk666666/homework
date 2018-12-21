### DDL语句以及插入数据练习

**创建一张学生表**

```
create table student(
	sid int primary key,
	sname varchar(10) not null,
	birthday datetime,
	sex char(1)
) auto_increment=1001;
```

**向学生表中插入数据**

```
load data infile 'C:\\Users\\WIN10\\Desktop\\java\\day22\\demo\\src\\mysql' into table student fields terminated by ',';
```

**创建教师表**

```
create table teacher(
	tname char(16) not null,
	tid int primary key auto_increment
);
```

**像教师表插入数据**

```
load data infile 'C:\\Users\\WIN10\\Desktop\\java\\day22\\demo\\src\\teacher' into table teacher fields terminated by ',';
```

**创建一张课程表**

```
create table cource(
	cid int primary key auto_increment,
	cname varchar(20) not null,
	tid int,
	foreign key(tid)  references teacher(tid)
);
```

**插入课程表数据**

```
load data infile 'C:\\Users\\WIN10\\Desktop\\java\\day22\\demo\\src\\cource' into table cource fields terminated by ',';
```

**创建成绩表**

```
create  table sc(
	sid int,
	cid int,
	score int,
	foreign key(sid) references student(sid),
	foreign key(cid) references cource(cid),
	primary key (sid,cid)
);
```

**插入成绩数据**

```
load data infile 'C:\\Users\\WIN10\\Desktop\\java\\day22\\demo\\src\\sc' into table sc fields terminated by ',';
```



### 单表查询练习



**查询姓“李”的老师的个数**

```
select count(tname) from teacher where tname like '李%';
```



**查询男女生人数个数**

```
select sex,count(*) from student group by sex;
```



**查询同名同姓学生名单，并统计同名人数**

```
select sname,count(sname) from student group by sname having count(sname)>1;
```



**1981年出生的学生名单**

```
select sname from student where year(birthday)=1981;
```



**查新平均成绩大于60分的同学的学号和平均成绩**

```
select sid,avg(score) from sc group by sid having avg(score)>60;
```



**求选了课程的学生人数**

```
 select count(distinct sid) from sc;
```



**查询至少选修两门课程的学生学号**

```
select sid from sc group by sid  having count(sid)>=2;
```



**查询各科成绩最高和最低的分。以如下形式显示：课程ID，最高分，最低分 **

```
select cid,max(score),min(score) from sc group by cid;
```



**统计每门课程的学生选修人数。要求输出课程号和选修人数，查询结果按人数降序排列，若人数相同，按课程号升序排列 **

```
select cid,count(sid) from sc group by cid  order by count(sid) desc,cid asc;
```



#### 以下练习针对部门员工表，请导入scott.sql的数据 



```
source C:/Users/WIN10/Desktop/java/day23/scott.sql
```

**打印入职时间超过38年的员工信息** 

```
select * from emp where (2018-year(hiredate))>=38;
```



**把hiredate列看做是员工的生日,求本月过生日的员工** 

```
select * from emp where month(hiredate)=12;
```



**把hiredate列看做是员工的生日,求下月过生日的员工** 

```
select * from emp where month(hiredate)=1;
```



**求1980年下半年入职的员工** 

```
select * from emp where year(hiredate)=1980 and month(hiredate)>6;
```



**请用两种的方式查询所有名字长度为4的员工的员工编号,姓名** 



方式一：

```
select empno,ename from emp where char_length(ename)=4;
```

方式二：

```
select empno,ename from emp where LENGTH(ename)=4;
```



**显示各种职位的最低工资** 

```
select job,min(mgr) from emp group by job;
```



**求1981年各个月入职的的员工个数** 

```
select month(hiredate),count(*) from emp where year(hiredate)=1981 group by month(hiredate);
```



**查询每个部门的最高工资** 

```
select deptno,max(mgr) from emp group by deptno;
```



**查询每个部门,每种职位的最高工资** 

```
select deptno,job,max(mgr) from emp group by job,deptno;
```



**查询各部门的总工资和平均工资** 

```
select deptno,sum(mgr),avg(mgr) from emp group by deptno;
```



**查询10号部门,20号部门的平均工资（尝试用多种写法）** 



```
select deptno,avg(mgr) from emp group by deptno having deptno=10 or deptno=20;
```

```
select deptno,avg(mgr) from emp group by deptno order by deptno asc limit 2;
```



**查询平均工资高于2000元的部门编号和平均工资** 

```
select deptno,avg(mgr) from emp group by deptno having avg(mgr)>2000;
```



**统计公司里经理的人数** 

```
select job,count(job) from emp group by job having job='MANAGER';
```



**查询工资最高的3名员工信息** 

```
select * from emp order by mgr desc limit 3;
```



**查询工资由高到低第6到第10的员工信息** 

```
select * from emp order by mgr desc limit 6,4;
```



### 表连接查询练习

 

**查询李四学习的课程，考试分数，课程的授课老师** 

```
select sc.sid,c.cname,sc.score,t.tname from sc left outer join student s on sc.sid=s.sid left outer join cource c on sc.cid=c.cid left outer join teacher t on c.tid=t.tid where sname='李四';
```



**查询王五有哪些课程没选，显示这些课程名称** 

```
select c.cname from cource c left outer join (select s.sname,sc.cid from sc inner join student s on sc.sid=s.sid where s.sname='王五') w on c.cid=w.cid where c.cid not in (1,2);
```



**查询所有同学的学号、姓名、选课数、总成绩** 

```
select sc.sid,s.sname,count(sc.cid),sum(sc.score) from sc left outer join student s on sc.sid=s.sid left outer join cource c on sc.cid=c.cid group by sc.sid;
```



**查询所有课程成绩都小于等于60分的同学的学号、姓名；** 

```
seleselect w.sid,student.sname from student inner join (select * from (select sid as m,count(sid) as s from sc group by sid) x inner  join (select sid,count(sid) as c from sc where  score<=60 group by sid) y on m=y.sid where s=c) w on w.sid=student.sid;
```



**查询没有学全所有课的同学的学号、姓名；** 

```
select s.sname,s.sid from student s inner join  (select sid,count(cid) from sc group by sid having count(cid)<7) w on  w.sid=s.sid;
```



**查询每门课程选修人数，格式为课程名称，人数** 

```
select c.cname,count(sc.cid) from sc left outer join cource c on sc.cid=c.cid group by sc.cid;
```



**查询出只选修了一门课程的全部学生的学号和姓名 ；** 

```
select s.sid,s.sname from sc left outer join student s on sc.sid=s.sid group by sc.sid having  count(sc.cid)=1;
```



**查询每门课程的平均成绩，结果按平均成绩升序排列，平均成绩相同时，按课程号降序排列** 

```
select c.cname,avg(score) from sc left outer join cource c on sc.cid=c.cid group by sc.cid order by avg(score) asc,sc.cid desc;
```



**查询学生平均成绩大于80的所有学生的学号、姓名和平均成绩** 

```
select s.sid,s.sname,avg(score) from sc left outer join student s on sc.sid=s.sid group by sc.sid having avg(score)>80;
```



**查询课程相同且成绩相同的的学生的学号、课程号、学生成绩** 

```
select sc.sid,sc.cid,sc.score from  sc inner join (select count(sid),cid,score from  sc group by cid,score having count(sid)>1) w on w.cid=sc.cid and w.score=sc.score;
```



**查询全部学生都选修的课程的课程号和课程名** 

```
select sc.cid,c.cname from sc inner join cource c on sc.cid=c.cid group by cid having count(sc.cid)=7;
```



**查询两门以上不及格课程的同学的学号及其平均成绩** 

```
select sid,count(sid) as nq,avg(score) from sc where score<60 group by sid having nq>=2;
```



### 子查询练习



**查询2号课程成绩比1号课程成绩低的学生的学号、姓名**

```sql
select a.sid,a.sname from (select m.sid,m.sname,m.score as x from (select sc.*,s.sname from sc inner join student s on sc.sid=s.sid) m where cid =1) a inner join (select sid,score as y from sc where cid =2) b on a.sid=b.sid where y<x;
```



**查询学过1号课程并且也学过编号2号课程的同学的学号、姓名**

```
select s.sname,x.sid  from student s inner join (select sid,count(sid) as c from sc where cid  in (1,2) group by sid having c=2) x on s.sid=x.sid;
```





**查询没学过“叶平”老师课的同学的学号、姓名**

```
select x.sid,x.sname from (select sc.sid,sc.cid,s.sname from sc inner join student s on sc.sid=s.sid) x  left outer join (select c.cid  as m from cource c inner join (select * from teacher where tname like '叶%') t on c.tid=t.tid) y on x.cid=m where m is NULL group by sid;
```

