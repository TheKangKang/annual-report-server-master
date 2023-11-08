#sql("list")
select * from login_info
where is_deleted = 0
#if(notBlank(rq.beginDate))
and date_format(created_time,'%Y-%m-%d') >= #para(rq.beginDate)
#end
#if(notBlank(rq.endDate))
and date_format(created_time,'%Y-%m-%d') <= #para(rq.endDate)
#end
#if(rq.loginType!=0)
and login_type = #para(rq.loginType)
#end
order by id desc
#end