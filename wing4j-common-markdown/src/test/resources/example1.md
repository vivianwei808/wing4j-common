����һ��markdown�ļ�����ʾ����
```wing4j configure
--��������
--@dialect wing4j
--�����ռ�
--@namespace org.wing4j.orm
```
#[��ѯ�û���Ϣ](selectDemo)#

����һ��markdown�ļ�����ʾ����
```wing4j param
--@flushCacheRequired=true //ǿ��ˢ�»���
--ʹ�û���ر�
--@useCache=false
--��ȡ��¼��С
--@fetchSize=1
--��ʱʱ��
--@timeout=1000
--���ע��
--@comment=��ע
```
```sql
select * 
from tb_demo_wing4j_inf t
where t.col1=#{col2:VRACHAR}
/*#     if col2 == null                  */
and col2=#{col2:VRACHAR}
/*#     fi                               */
/*#     if col3 is not null              */
and col3=#{col2:NUMBER}
/*#     fi                               */
```

[��������](insert)
================================

```sql
insert into table1(col1, col2, col3)
values('col1', 'col2', 3)
```

[�����û�ID��������](updateById)
================================

```sql
update table t
set t.col1 = #{col2:VRACHAR}
where t.col1='col1'
/*#     if col2 is not null              */
and col2=$col2:VRACHAR$
/*#     fi                               */
/*#     if col3 is not null              */
and col3=$col3:NUMBER$
/*#     fi                               */
```


[�����û�IDɾ������](deleteById)
================================

```sql
delete from table t
where t.col1='col1'
/*#     if col2 is not null              */
and col2=$col2:VRACHAR$
/*#     fi                               */
/*#     if col3 is not null              */
and col3=$col3:NUMBER$
/*#     fi                               */
```