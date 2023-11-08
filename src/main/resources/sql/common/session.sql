#sql("getByToken")
select id,user_id,session_id,sex
from session
where session_id = #para(sessionId) and is_deleted = 0
#end

#sql("getByAccount")
select id,session_id
from session
where user_id = #para(accountId) and is_deleted = 0
order by id desc
#end

#sql("deleteByToken")
update student_session set is_deleted = 1
where session_id = #para(token)
#end