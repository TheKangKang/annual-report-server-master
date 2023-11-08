#sql("getBillByAccount")
select * from bill where identification=#para(account) and is_deleted=0
#end
--根据学号选择某位学生总表的所有信息

#sql("getName")
select name,identification from bill
where identification=#para(account) and is_deleted=0
#end
--根据学号选择某位学生的姓名和学号

-- //分页获取数据
-- //上课篇
-- #sql("getClassOneData")
-- select sum_lesson, sum_lesson_time, sum_morning_lesson, sum_evening_lesson
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end
--
-- #sql("getClassTwoData")
-- select most_buliding, most_buliding_lesson, most_classroom, most_classroom_lesson
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end
--
-- //食堂篇
-- #sql("getCanteenOneData")
-- select most_consume_money_canteen, most_consume_canteen_money
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end
--
-- #sql("getCanteenTwoData")
-- select sum_charge, most_consume_money_canteen_date, most_consume_money_canteen_oneday
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end
--
-- //信息化篇
-- #sql("getNetworkOneData")
-- select sum_web_time, sum_web_flow
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end
--
-- #sql("getNetworkTwoData")
-- select longest_web_use, lastest_web_login
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end
--
-- //图书馆篇
-- #sql("getLibraryOneData")
-- select library_appointment_time, library_appointment_time_rank, sum_study_time
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end
--
-- #sql("getLibraryTwoData")
-- select sum_borrow_book, sum_borrow_book_rank
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end
--
-- //运动篇
-- #sql("getSportData")
-- select sum_appointment, sum_appointment_rank
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end
--
-- //结尾篇
-- #sql("getEndData")
-- select end_achievement
-- from bill
-- where identification = #para(account) and is_deleted = 0
-- #end


