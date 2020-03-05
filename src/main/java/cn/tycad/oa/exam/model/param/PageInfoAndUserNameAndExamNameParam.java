package cn.tycad.oa.exam.model.param;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author YY
 * @date 2019/4/18
 * @description
 */
@Data
@NoArgsConstructor
public class PageInfoAndUserNameAndExamNameParam {
    private Integer pageNum;
    private Integer pageSize;
    private String examName;
    private String userName;
    private String[] departmentIds;
    private String companyName;
    private Date min;
    private Date max;

    public PageInfoAndUserNameAndExamNameParam(String examName) {
        this.examName = examName;
    }
    public PageInfoAndUserNameAndExamNameParam(String examName,String userName) {
        this.userName = userName;
        this.examName = null;
    }
}
