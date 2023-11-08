#sql("get")
select * from student where id=#para(id) and is_deleted=0
#end

#sql("list")
select * from student where is_deleted=0
#end