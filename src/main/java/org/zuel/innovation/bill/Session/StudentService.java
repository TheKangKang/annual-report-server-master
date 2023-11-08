package org.zuel.innovation.bill.Session;

import cn.fabrice.jfinal.service.BaseService;
import org.zuel.innovation.common.module.Student;

/**
 * @author lxree
 */
public class StudentService extends BaseService<Student> {
    public StudentService(){
        super("student.",Student.class,"student");
    }
}
