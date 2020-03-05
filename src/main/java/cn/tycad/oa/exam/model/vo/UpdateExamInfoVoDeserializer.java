package cn.tycad.oa.exam.model.vo;

import cn.tycad.oa.exam.exception.BusinessException;
import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: shizf
 * @Date: 20190605
 * @Description: 更新考试信息的反序列化类，在反序列化时，提供更详细的异常信息
 */
public class UpdateExamInfoVoDeserializer extends JsonDeserializer {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public UpdateExamInfoVo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String examId = node.get("examId").textValue();
        String examName = node.get("examName").textValue();
        String subTitle = node.get("subTitle").textValue();
        String description = node.get("description").textValue();
        String strCreateTime = node.get("createTime").textValue();
        String creatorId = node.get("creator").textValue();
        String strPublishTime = node.get("publishTime").textValue();
        String strDuration = String.valueOf(node.get("duration").textValue());
        String strDeadLine = node.get("deadLine").textValue();
        JsonNode jsonUserIds = node.get("userIds");

        Date createTime = null;
        Date lastEditTime = null;
        Date publishTime = null;
        Date deadline = null;
        Integer duration = null;

        // 校验examId，不能为空
        if (StringUtils.isBlank(examId))
            throw new BusinessException(ExceptionInfoEnum.BLANK_EXAM_ID);

        // 校验examName，不能为空，长度小于50
        if (StringUtils.isBlank(examName))
            throw new BusinessException(ExceptionInfoEnum.BLANK_EXAM_NAME);

        if (examName.trim().length() > 50)
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_EXAM_NAME_OVER_SIZE);

        // 校验subtitle，长度不能超过100
        if (StringUtils.isNotBlank(subTitle) && subTitle.trim().length() > 100)
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_SUBTITLE_OVER_SIZE);

        // 校验createTime
        if (StringUtils.isNotBlank(strCreateTime)) {
            try {
                createTime = SDF.parse(strCreateTime.trim());
            } catch (ParseException e1) {
                throw new BusinessException(ExceptionInfoEnum.CREATE_TIME_ERROR);
            }
        }

        // 校验creatorId
        if (StringUtils.isBlank(creatorId))
            throw new BusinessException(ExceptionInfoEnum.BLANK_EXAM_CREATOR_ID);

        // 校验publishTime
        if (StringUtils.isBlank(strPublishTime))
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_PUBLISH_TIME_ERROR);

        try {
            publishTime = SDF.parse(strPublishTime.trim());
        } catch (ParseException e2) {
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_PUBLISH_TIME_ERROR);
        }

        // 校验duration
        if (StringUtils.isBlank(strDuration))
            throw new BusinessException(ExceptionInfoEnum.BLANK_EXAM_DURATION);

        try {
            duration = Integer.parseInt(strDuration);
        } catch (NumberFormatException e3) {
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_DURATION_INVALID_ERROR);
        }

        // 校验deadline
        if (StringUtils.isBlank(strDeadLine))
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_DEADLINE_INVALID_ERROR);

        try {
            deadline = SDF.parse(strDeadLine.trim());
        } catch (ParseException e3) {
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_DEADLINE_INVALID_ERROR);
        }

        // 获取userIds
        Iterator<JsonNode> userIdsElements = jsonUserIds.iterator();
        Set<String> userIds = new HashSet<>();
        while (userIdsElements.hasNext()) {
            JsonNode element = userIdsElements.next();
            userIds.add(element.toString().replace("\"", ""));
        }

        return new UpdateExamInfoVo(examId, examName, subTitle, description, createTime, creatorId,
                null, null, publishTime, duration, deadline, userIds);
    }
}
