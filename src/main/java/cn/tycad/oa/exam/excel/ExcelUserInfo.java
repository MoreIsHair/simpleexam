package cn.tycad.oa.exam.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

/**
 * @Author: shizf
 * @Date: 0410
 * @Description: Excel对应的人员对象
 */
@Data
public class ExcelUserInfo extends BaseRowModel {
    @ExcelProperty(value = {"用户名"}, index = 0)
    private String userName;
    @ExcelProperty(value = {"姓名"}, index = 1)
    private String name;
    @ExcelProperty(value = {"密码"}, index = 2)
    private String password;
    @ExcelProperty(value = {"性别"}, index = 3)
    private String gender;
    @ExcelProperty(value = {"说明"}, index = 4)
    private String description;
    @ExcelProperty(value = {"员工类型"},index = 5)
    private String typeString;
    @ExcelProperty(value = {"生日"} , index = 6, format = "yyyy-MM-dd")
    private Date birthday;
    @ExcelProperty(value = {"部门"} , index = 7)
    private String dept;
    @ExcelProperty(value = {"公司名称"} , index = 8)
    private String companyName;
    @ExcelProperty(value = {"用户组"} , index = 9)
    private String group;
    @ExcelProperty(value = {"角色"} , index = 10)
    private String role;


}
