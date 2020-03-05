package cn.tycad.oa.exam.service;

import cn.tycad.oa.exam.common.util.CommonUtils;
import cn.tycad.oa.exam.exception.BusinessException;
import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.model.bo.TbReviewAndExamBo;
import cn.tycad.oa.exam.model.entity.TbReview;
import cn.tycad.oa.exam.model.param.ReviewAvgParam;
import cn.tycad.oa.exam.model.vo.ReviewAllOptionAvgVo;
import cn.tycad.oa.exam.model.vo.TbReviewAvgAndExamRankVo;
import cn.tycad.oa.exam.repository.TbReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: shizf
 * @Date: 20190404
 * @Description: 用户评分Service
 */
@Service
public class ReviewService {
    @Autowired
    private TbReviewMapper reviewMapper;

    public void insert(TbReview review) {
        if (review.getReviewId().isEmpty()) {
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }

        //检查是否重复写入
        if (reviewMapper.getCountByExamIdAndUserId(review.getExamId(), review.getUserId(),null) > 0) {
            throw new BusinessException(ExceptionInfoEnum.DUPLICATE_INSERT);
        }

        int totalNum = review.getAnswerOnQuestion() + review.getExperience()
                + review.getHandoutQuality() + review.getHelpful()
                + review.getInteraction() + review.getLogic()
                + review.getSensible() + review.getPronunciation()
                + review.getUnderstandable() + review.getWellPrepared();

        if (review.getTotalScore() != totalNum) {
            review.setTotalScore(totalNum);
        }

        reviewMapper.insert(review);
    }

    public List<TbReview> getList() {
        return reviewMapper.getList();
    }

    public List<TbReview> getListByExamId(String examId, String userId) {
        return reviewMapper.getListByExamId(examId, userId);
    }

    /**
     * 1.显示所有课程的平均分排名（所有参考人对某次考试的总评分的平均分 * 所有考试）
     * 2.显示所有老师的平均分排名（一个老师所有考试的总评分的平均分 * 所有老师）
     * 3.使用雷达图显示所有老师的平均分（每个雷达图显示一位讲师的分数）（某个老师的所有考试（是否考虑考试不同所占的权值）的某个评价点的 * 所有参考人 * 平均分 ）
     * @param min 时间最小值
     * @param max 时间最大值
     */
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public Map<String, Object> getRankList(Date min, Date max){
        if (min == null|| min.after(new Date())){
            HashMap<Integer, Integer> time = new HashMap<>(16);
            time.put(Calendar.YEAR, -1);
            min = CommonUtils.time(new Date(),time);
            max = null;
        }
        if (max == null || max.after(new Date())){
            max = new Date();
        }
        List<TbReviewAndExamBo> allExamListByExamIdSort = reviewMapper.getAllExamListByExamIdSort(min, max);
        // 按照考试Id分开
        Map<String, ArrayList<TbReviewAndExamBo>> collectByExamId = allExamListByExamIdSort.stream().collect(Collectors.toMap(e -> e.getExamId(), e -> {
            ArrayList<TbReviewAndExamBo> arrayList = new ArrayList();
            arrayList.add(e);
            return arrayList;
        }, (key1, key2) -> {
            key2.addAll(key1);
            return key2;
        }));
        ArrayList<TbReviewAvgAndExamRankVo> tbReviewAvgAndExamRankVos = new ArrayList<>();
        for (String s : collectByExamId.keySet()) {
            TbReviewAvgAndExamRankVo tbReviewAvgAndExamRankVo = new TbReviewAvgAndExamRankVo();
            ArrayList<TbReviewAndExamBo> arrayList = collectByExamId.get(s);
            Integer reduce = arrayList.stream().filter(e -> s.equals(e.getExamId())).map(TbReviewAndExamBo::getTotalScore).reduce(0, (a, b) -> a + b);
            tbReviewAvgAndExamRankVo.setTbReviewAndExamBos(arrayList);
            tbReviewAvgAndExamRankVo.setAverage((reduce * 1.0) / arrayList.size());
            tbReviewAvgAndExamRankVos.add(tbReviewAvgAndExamRankVo);
        }
        //  1.所有课程的平均分排名(排序)
        List<TbReviewAvgAndExamRankVo> collect1 = tbReviewAvgAndExamRankVos.stream().sorted(Comparator.comparing(TbReviewAvgAndExamRankVo::getAverage)).collect(Collectors.toList());
        //  2.所有老师的平均分排名
        Map<String, ArrayList<TbReviewAndExamBo>> collectByTeacherName = allExamListByExamIdSort.stream().collect(
                Collectors.toMap(TbReviewAndExamBo::getTeacherName, e -> {
            ArrayList<TbReviewAndExamBo> arrayList = new ArrayList();
            arrayList.add(e);
            return arrayList;
        }, (key1, key2) -> {
            key2.addAll(key1);
            return key2;
        }));
        ArrayList<TbReviewAvgAndExamRankVo> tbReviewAvgAndExamRankVos2 = new ArrayList<>();

        // 3.使用雷达图显示所有老师的平均分（用来总分的的其他评估点分数）
        List<ReviewAllOptionAvgVo> collect3 = new ArrayList<>(16);
        List collect4 = new ArrayList<>(16);

        for (String s : collectByTeacherName.keySet()) {
            ArrayList<TbReviewAndExamBo> arrayList = collectByTeacherName.get(s);
            Set<String> examIds = arrayList.stream().map(a -> a.getExamId()).collect(Collectors.toSet());
            ArrayList<ReviewAvgParam> reviewAvgParams = new ArrayList<>();
            HashMap<String, Double> stringDoubleHashMap = new HashMap<>(16);
            HashMap<String, ReviewAvgParam> stringReviewAvgParamHashMap = new HashMap<>(16);
            // 分开得出属于一个老师的多个考试（先求出一个老师一场考试的所有评价人员的平均分）
            for (String examId : examIds) {
                // 同一场考试的评价分
                List<TbReviewAndExamBo> collect = arrayList.stream().filter(a -> a.getExamId().equals(examId)).collect(Collectors.toList());
                // 总分
                Integer reduce = collect.stream().map(c -> c.getTotalScore()).reduce(0, (x, y) -> x + y);
                stringDoubleHashMap.put(examId,getAvg(collect, reduce));
                // 一场考试其他评估点
                Integer answerOnQuestion = collect.stream().map(c -> c.getAnswerOnQuestion()).reduce(0, (x, y) -> x + y);
                Integer experience = collect.stream().map(c -> c.getExperience()).reduce(0, (x, y) -> x + y);
                Integer handoutQuality = collect.stream().map(c -> c.getHandoutQuality()).reduce(0, (x, y) -> x + y);
                Integer helpful = collect.stream().map(c -> c.getHelpful()).reduce(0, (x, y) -> x + y);
                Integer interaction = collect.stream().map(c -> c.getInteraction()).reduce(0, (x, y) -> x + y);
                Integer logic = collect.stream().map(c -> c.getLogic()).reduce(0, (x, y) -> x + y);
                Integer sensible = collect.stream().map(c -> c.getSensible()).reduce(0, (x, y) -> x + y);
                Integer understandable = collect.stream().map(c -> c.getUnderstandable()).reduce(0, (x, y) -> x + y);
                Integer wellPrepared = collect.stream().map(c -> c.getWellPrepared()).reduce(0, (x, y) -> x + y);
                Integer pronunciation = collect.stream().map(c -> c.getPronunciation()).reduce(0, (x, y) -> x + y);
                ReviewAvgParam reviewAvgParam = new ReviewAvgParam(examId, getAvg(collect, wellPrepared),
                        getAvg(collect, pronunciation), getAvg(collect, experience), getAvg(collect, logic), getAvg(collect, understandable), getAvg(collect, handoutQuality),
                        getAvg(collect, interaction), getAvg(collect, answerOnQuestion), getAvg(collect, helpful), getAvg(collect, sensible), getAvg(collect, reduce));
                reviewAvgParam.setExamId(collect.get(0).getExamName());
                reviewAvgParams.add(reviewAvgParam);
                stringReviewAvgParamHashMap.put(examId,reviewAvgParam);
            }
            Double reduce1 = stringDoubleHashMap.values().stream().reduce(new Double(0), (x, y) -> x + y);
            // 求出一位老师所有考试总分的平均分
            double v = getAvg(examIds,reduce1);
            TbReviewAvgAndExamRankVo tbReviewAvgAndExamRankVo = new TbReviewAvgAndExamRankVo();
            tbReviewAvgAndExamRankVo.setAverage(v);
            tbReviewAvgAndExamRankVo.setTbReviewAndExamBos(arrayList);
            tbReviewAvgAndExamRankVos2.add(tbReviewAvgAndExamRankVo);

            // 先求出其他各项评估点总分，再求平均分
            Double answerOnQuestionAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getAnswerOnQuestion()).reduce(new Double(0), (x, y) -> x + y);
            Double experienceAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getExperience()).reduce(new Double(0), (x, y) -> x + y);
            Double handoutQualityAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getHandoutQuality()).reduce(new Double(0), (x, y) -> x + y);
            Double helpfulAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getHelpful()).reduce(new Double(0), (x, y) -> x + y);
            Double interactionAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getInteraction()).reduce(new Double(0), (x, y) -> x + y);
            Double logicAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getLogic()).reduce(new Double(0), (x, y) -> x + y);
            Double sensibleAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getSensible()).reduce(new Double(0), (x, y) -> x + y);
            Double understandableAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getUnderstandable()).reduce(new Double(0), (x, y) -> x + y);
            Double wellPreparedAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getWellPrepared()).reduce(new Double(0), (x, y) -> x + y);
            Double pronunciationAll = stringReviewAvgParamHashMap.values().stream().map(e -> e.getPronunciation()).reduce(new Double(0), (x, y) -> x + y);
            ReviewAllOptionAvgVo reviewAllOptionAvgVo = new ReviewAllOptionAvgVo(getAvg(examIds, wellPreparedAll), getAvg(examIds, pronunciationAll), getAvg(examIds, experienceAll), getAvg(examIds, logicAll)
                    , getAvg(examIds, understandableAll), getAvg(examIds, handoutQualityAll), getAvg(examIds, interactionAll), getAvg(examIds, answerOnQuestionAll)
                    , getAvg(examIds, helpfulAll), getAvg(examIds, sensibleAll), s);
            // 一位老师的其他各项评估点的平均分封装的对象
            collect3.add(reviewAllOptionAvgVo);


            HashMap<String, List<ReviewAvgParam>> stringReviewAvgParamHashMap1 = new HashMap<>(16);
            HashMap<String, String> teacherNameMap = new HashMap<>(16);
            teacherNameMap.put("teacherName",s);
            stringReviewAvgParamHashMap1.put("data",reviewAvgParams);
            HashMap<String, Map> stringMapHashMap = new HashMap<>();
            stringMapHashMap.put("data",stringReviewAvgParamHashMap1);
            stringMapHashMap.put("name",teacherNameMap);
            collect4.add(stringMapHashMap);
        }
        // 2.所有老师的平均分排名(排序)
        List<TbReviewAvgAndExamRankVo> collect2 = tbReviewAvgAndExamRankVos2.stream().sorted(Comparator.comparing(e -> e.getAverage())).collect(Collectors.toList());
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(16);
        stringObjectHashMap.put("ByExamRank",collect1);
        stringObjectHashMap.put("ByTeacherNameRank",collect2);
        stringObjectHashMap.put("ByTeacherNameOptionList",collect4);
        return stringObjectHashMap;
    }

    private double getAvg(Set<String> examIds, Double answerOnQuestionAll) {
        BigDecimal b = new BigDecimal(answerOnQuestionAll / examIds.size());
        // 四舍五入
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double getAvg(List<TbReviewAndExamBo> collect, Integer reduce) {
        BigDecimal b = new BigDecimal(reduce / (collect.size() * 1.0));
        // 四舍五入
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
