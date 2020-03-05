package cn.tycad.oa.exam.excel;

import cn.tycad.oa.exam.exception.BusinessException;
import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: YY
 * @Date: 2019/3/18
 * @Description:
 */
@Data
public class ExcelQuestionInfo extends BaseRowModel {
    @ExcelProperty(value = {"题目"},index = 0)
    private String questionTitle;
    @ExcelProperty(value = {"题干"},index = 1)
    private String questionText;
    @ExcelProperty(value = {"类型"},index = 2)
    private String questionTypeString;
    private Integer questionType;
    @ExcelProperty(value = {"选项一"},index = 3)
    private String optionOne;
    @ExcelProperty(value = {"选项二"},index = 4)
    private String optionTwo;
    @ExcelProperty(value = {"选项三"},index = 5)
    private String optionThree;
    @ExcelProperty(value = {"选项四"},index = 6)
    private String optionFour;
    @ExcelProperty(value = {"选项五"},index = 7)
    private String optionFive;
    @ExcelProperty(value = {"选项六"},index = 8)
    private String optionSix;
    @ExcelProperty(value = {"正确选项"},index = 9)
    private String validAnswer;
    @ExcelProperty(value = {"分数"},index = 10)
    private Float questionScore;

    public boolean isValid() {
        if ("".equals(this.questionTitle) || this.questionTitle == null) {return false;}
        if (StringUtils.isBlank(this.questionTypeString)){
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_QUESTION_TYPE_NOT_VALID);
        }
        this.setQuestionType();
        if (questionType == -1 || questionType == 0) {
            //选择题选项不能全为空
            if (("".equals(this.optionOne) || this.optionOne == null)
                    && ("".equals(this.optionTwo) || this.optionTwo == null)
                    && ("".equals(this.optionThree) || this.optionThree == null)
                    && ("".equals(this.optionFour) || this.optionFour == null)
                    && ("".equals(this.optionFive) || this.optionFive == null)
                    && ("".equals(this.optionSix) || this.optionSix == null)) {
                return false;
            }

            //选择题标准答案不能为空
            if (validAnswer == null) return false;
        }

        if (questionType == 1 && validAnswer == null) return false;

        if (questionScore == null) return false;

        return true;
    }
    /**
     * 将数字表示题目类型修转换为文字表示题目类型
     */
    private void setQuestionType() {
        switch (this.questionTypeString.trim()){
            case "单项选择题":{
                this.questionType = 0;
                break;
            }
            case "多项选择题":{
                this.questionType = -1;
                break;
            }
            case "判断题":{
                this.questionType = 1;
                break;
            }
            case "简答题":{
                this.questionType = 2;
                break;
            }
            default: {
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_QUESTION_TYPE_NOT_VALID);
            }
        }
    }
}
